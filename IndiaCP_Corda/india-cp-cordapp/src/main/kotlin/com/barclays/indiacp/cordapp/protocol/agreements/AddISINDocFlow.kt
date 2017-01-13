package com.barclays.indiacp.cordapp.protocol.agreements

import co.paralleluniverse.fibers.Suspendable
import com.barclays.indiacp.cordapp.contract.IndiaCommercialPaperProgram
import com.barclays.indiacp.model.CPProgramError
import com.barclays.indiacp.model.Error
import com.barclays.indiacp.model.IndiaCPException
import net.corda.core.contracts.StateAndRef
import net.corda.core.crypto.CompositeKey
import net.corda.core.crypto.Party
import net.corda.core.crypto.SecureHash
import net.corda.core.flows.FlowLogic
import net.corda.core.node.NodeInfo
import net.corda.core.node.PluginServiceHub
import net.corda.core.seconds
import net.corda.core.serialization.SingletonSerializeAsToken
import net.corda.core.transactions.SignedTransaction
import net.corda.core.transactions.TransactionBuilder
import net.corda.core.utilities.Emoji
import net.corda.core.utilities.ProgressTracker
import net.corda.flows.FetchAttachmentsFlow
import net.corda.flows.TwoPartyDealFlow
import java.security.KeyPair
import java.util.*
import javax.ws.rs.core.Response
import kotlin.test.assertEquals

/**
 * This is the Flow to manage the consensus between Issuer and Depository (NSDL) when requesting generation
 * of ISIN Identifier for the Commercial Paper Program. This is depicting the current paper flow, though digitized
 * and stamped on the blockchain.
 * The actual ISIN issuance is a separate flow. This flow is a pre-requisite to that.
 *
 * Created by ritukedia
 */
class AddISINDocFlow(val contractStateAndRef: StateAndRef<IndiaCommercialPaperProgram.State>) : FlowLogic<SignedTransaction>() {

    class Services(services: PluginServiceHub) : SingletonSerializeAsToken() {

        init {
            services.registerFlowInitiator(ISINRequestInitiator::class) { ISINRequestAcceptor(it) }
        }
    }

    companion object {

        object INITIATING_ISIN_AGREEMENT : ProgressTracker.Step("Sending the ISIN Letter of Intent to NSDL") {
            override fun childProgressTracker(): ProgressTracker = TwoPartyDealFlow.Primary.tracker()
        }
        object RECEIVED_ACCEPTANCE : ProgressTracker.Step("Received Depository Response")

        fun tracker() = ProgressTracker(INITIATING_ISIN_AGREEMENT, RECEIVED_ACCEPTANCE)
    }

    override val progressTracker: ProgressTracker = AddISINDocFlow.tracker()

    val acceptorParty: Party = contractStateAndRef.state.data.depository

    val startingProgressStep: ProgressTracker.Step = INITIATING_ISIN_AGREEMENT

    @Suspendable
    override fun call(): SignedTransaction {
        require(serviceHub.networkMapCache.notaryNodes.isNotEmpty()) { "No notary nodes registered" }
        val notary = serviceHub.networkMapCache.notaryNodes.first().notaryIdentity
        progressTracker.currentStep = startingProgressStep
        val instigator = ISINRequestInitiator(
                acceptorParty,
                IndiaCPPayload(contractStateAndRef, notary),
                serviceHub.legalIdentityKey,
                progressTracker.getChildProgressTracker(startingProgressStep)!!)
        val stx = subFlow(instigator)
        return stx
    }

}

/**
 * Initiator Side of the Flow for any of the two party agreement calls
 * For e.g. In case of ISIN Request a Letter of Intent request is sent by the Issuer to the Depository
 * In this example the Issuer is the Request Initiator and the Depository is the Request Acceptor
 * The Request Acceptor validates the Payload and Creates the Agreed Shared Transaction
 */
open class ISINRequestInitiator(override val otherParty: Party,
                            override val payload: IndiaCPPayload,
                            override val myKeyPair: KeyPair,
                            override val progressTracker: ProgressTracker = TwoPartyDealFlow.Primary.tracker()) : TwoPartyDealFlow.Primary() {

    override val notaryNode: NodeInfo get() =
    serviceHub.networkMapCache.notaryNodes.filter { it.notaryIdentity == payload.notary }.single()

}

/**
 * Depository Side of the Flow for Accepting the ISIN Request Documents. The actual ISIN will be assigned
 * in a separate flow. See the addISIN endpoint in IndiaCPProgramApi
 */
open class ISINRequestAcceptor(override val otherParty: Party,
                               override val progressTracker: ProgressTracker = TwoPartyDealFlow.Secondary.tracker()) : TwoPartyDealFlow.Secondary<IndiaCPPayload>() {

    override fun validateHandshake(handshake: TwoPartyDealFlow.Handshake<IndiaCPPayload>): TwoPartyDealFlow.Handshake<IndiaCPPayload> {
        val isinDocAgreement = handshake.payload
        val cpProgramStateAndRef = isinDocAgreement.contractStateAndRef
        if (cpProgramStateAndRef.state.data is IndiaCommercialPaperProgram.State) {
            return handshake
        }
        else
            throw IndiaCPException("Unexpected '${cpProgramStateAndRef.state.data.javaClass.simpleName}' Payload Received in ${this.javaClass.simpleName} Flow Logic. Expected: ${IndiaCommercialPaperProgram.State::class.java.name}", Error.SourceEnum.DL_R3CORDA)
    }

    override fun assembleSharedTX(handshake: TwoPartyDealFlow.Handshake<IndiaCPPayload>): Pair<TransactionBuilder, List<CompositeKey>> {
        val cpProgramStateAndRef = handshake.payload.getCPProgramStateAndRef()
        val isinDocHash = SecureHash.parse(cpProgramStateAndRef.state.data.isinGenerationRequestDocId?.split(":")?.first()!!)

        val tx = IndiaCommercialPaperProgram().generateTransactionWithISINDocAttachment(cpProgramStateAndRef, handshake.payload.notary)

        // And add a request for timestamping
        tx.setTime(serviceHub.clock.instant(), 30.seconds)

        //Register Subscriber for Resolving Attachment
//        serviceHub.storageService.validatedTransactions.updates.subscribe { event ->
//            // When the transaction is received, it's passed through [ResolveTransactionsFlow], which first fetches any
//            // attachments for us, then verifies the transaction. As such, by the time it hits the validated transaction store,
//            // we have a copy of the attachment.
//            val tx = event.tx
//            if (tx.attachments.isNotEmpty()) {
//                val attachment = serviceHub.storageService.attachments.openAttachment(tx.attachments.first())
//                assertEquals(SecureHash.parse(cpProgramStateAndRef.state.data.isinGenerationRequestDocId?.split(":")?.first()!!), attachment?.id)
//                println("ISIN Request DOC received on NSDL Node!\n\nFinal transaction is:\n\n${Emoji.renderIfSupported(event.tx)}")
//            } else {
//                throw IndiaCPException(CPProgramError.DOC_UPLOAD_ERROR, Error.SourceEnum.DL_R3CORDA)
//            }
//        }
        subFlow(FetchAttachmentsFlow(setOf(isinDocHash), cpProgramStateAndRef.state.data.issuer))

        return Pair(tx, arrayListOf(cpProgramStateAndRef.state.data.depository.owningKey))
    }

    companion object {

        object ACCEPTING_ISIN_AGREEMENT : ProgressTracker.Step("Accepting ISIN Generation Letter of Intent Doc") {
            override fun childProgressTracker(): ProgressTracker = TwoPartyDealFlow.Secondary.tracker()
        }

        fun tracker() = ProgressTracker(ACCEPTING_ISIN_AGREEMENT)

    }

    //TODO: If we pass this parent progress tracker then the child progress tracker i.e. TwoPartyDealFlow.Secondary.tracker()
    //steps are not being recognized
    //override val progressTracker = ISINRequestAcceptor.tracker()

}
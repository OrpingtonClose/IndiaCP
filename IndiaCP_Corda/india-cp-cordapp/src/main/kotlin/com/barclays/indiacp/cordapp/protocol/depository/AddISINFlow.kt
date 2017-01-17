package com.barclays.indiacp.cordapp.protocol.depository

import co.paralleluniverse.fibers.Suspendable
import com.barclays.indiacp.cordapp.contract.IndiaCommercialPaper
import com.barclays.indiacp.cordapp.contract.IndiaCommercialPaperProgram
import com.barclays.indiacp.cordapp.protocol.agreements.IndiaCPDocumentPayload
import com.barclays.indiacp.cordapp.utilities.CPUtils
import com.barclays.indiacp.model.Error
import com.barclays.indiacp.model.IndiaCPDocumentDetails
import com.barclays.indiacp.model.IndiaCPException
import net.corda.core.contracts.StateAndRef
import net.corda.core.crypto.CompositeKey
import net.corda.core.crypto.Party
import net.corda.core.flows.FlowLogic
import net.corda.core.node.NodeInfo
import net.corda.core.node.PluginServiceHub
import net.corda.core.seconds
import net.corda.core.serialization.SingletonSerializeAsToken
import net.corda.core.transactions.SignedTransaction
import net.corda.core.transactions.TransactionBuilder
import net.corda.core.utilities.ProgressTracker
import net.corda.flows.TwoPartyDealFlow
import java.security.KeyPair

/**
 * This is the Flow to manage the consensus between Issuer and Depository (NSDL) when sending the newly generated
 * ISIN Identifier for the Commercial Paper Program from NSDL to Issuer. This is depicting the current paper flow,
 * though digitized and stamped on the blockchain.
 * The flow is preceeded by the ISIN Generation Request Flow where the Letter of Intent is uploaded to the DL.
 *
 * Created by ritukedia
 */
class AddISINFlow(val contractStateAndRef: StateAndRef<IndiaCommercialPaperProgram.State>) : FlowLogic<SignedTransaction>() {

    class Services(services: PluginServiceHub) : SingletonSerializeAsToken() {

        init {
            services.registerFlowInitiator(ISINCreationInitiator::class) { ISINCreationAcceptor(it) }
        }
    }

    companion object {

        object INITIATING_ISIN_CREATION : ProgressTracker.Step("Sending the newly generated ISIN to the Issuer") {
            override fun childProgressTracker(): ProgressTracker = TwoPartyDealFlow.Primary.tracker()
        }
        object RECEIVED_ACCEPTANCE : ProgressTracker.Step("Received Issuer Acceptance Response")
        object COPYING_TO_PARTICIPANTS : ProgressTracker.Step("Propogating transaction to all participants")

        // We vend a progress tracker that already knows there's going to be a TwoPartyTradingFlow involved at some
        // point: by setting up the tracker in advance, the user can see what's coming in more detail, instead of being
        // surprised when it appears as a new set of tasks below the current one.
        fun tracker() = ProgressTracker(INITIATING_ISIN_CREATION, RECEIVED_ACCEPTANCE, COPYING_TO_PARTICIPANTS)
    }

    override val progressTracker: ProgressTracker = tracker()

    val acceptorParty: Party = contractStateAndRef.state.data.issuer

    val startingProgressStep: ProgressTracker.Step = INITIATING_ISIN_CREATION

    @Suspendable
    override fun call(): SignedTransaction {
        require(serviceHub.networkMapCache.notaryNodes.isNotEmpty()) { "No notary nodes registered" }
        val notary = serviceHub.networkMapCache.notaryNodes.first().notaryIdentity
        val myKey = serviceHub.legalIdentityKey
        progressTracker.currentStep = startingProgressStep
        val instigator = ISINCreationInitiator(
                acceptorParty,
                IndiaCPDocumentPayload(contractStateAndRef, IndiaCPDocumentDetails.DocTypeEnum.DEPOSITORY_DOCS, notary),
                myKey,
                progressTracker.getChildProgressTracker(startingProgressStep)!!)
        val stx = subFlow(instigator)

        progressTracker.currentStep = COPYING_TO_PARTICIPANTS
        val parties = contractStateAndRef.state.data.parties
        val initiator = serviceHub.myInfo.legalIdentity
        if (parties.isNotEmpty()) {
            // Copy the transaction to other participant nodes
            parties.filter{!it.equals(initiator) && !it.equals(acceptorParty)}.forEach { send(it, stx) }
        }

        //propogate to participants of the CP Issues
        val cpIssues : List<StateAndRef<IndiaCommercialPaper.State>> = CPUtils.getAllCP(serviceHub, contractStateAndRef.state.data.programId)
        for (cpIssue in cpIssues) {
            val cpIssueParties = cpIssue.state.data.parties
            if (cpIssueParties.isNotEmpty()) {
                // Copy the transaction to other participant nodes
                cpIssueParties.subtract(parties).forEach { send(it, stx) }
            }
        }

        return stx
    }

}

/**
 * NSDL Side of the Flow for Initiating the New ISIN Creation Flow
 */
open class ISINCreationInitiator(override val otherParty: Party,
                                 override val payload: IndiaCPDocumentPayload,
                                 override val myKeyPair: KeyPair,
                                 override val progressTracker: ProgressTracker = TwoPartyDealFlow.Primary.tracker()) : TwoPartyDealFlow.Primary() {

    override val notaryNode: NodeInfo get() =
    serviceHub.networkMapCache.notaryNodes.filter { it.notaryIdentity == payload.notary }.single()

}

/**
 * Issuer Side of the Flow for Accepting the Newly Generated ISIN.
 * We could plug in some verification logic to ensure that the generated ISIN is authentic.
 * At the moment it assumes that the signature of the Depository on the transaction is sufficient as a proof of authenticity
 */
open class ISINCreationAcceptor(override val otherParty: Party,
                                override val progressTracker: ProgressTracker = TwoPartyDealFlow.Secondary.tracker()) : TwoPartyDealFlow.Secondary<IndiaCPDocumentPayload>() {

    override fun validateHandshake(handshake: TwoPartyDealFlow.Handshake<IndiaCPDocumentPayload>): TwoPartyDealFlow.Handshake<IndiaCPDocumentPayload> {
        val cpProgramStateAndRef = handshake.payload.contractStateAndRef
        if (cpProgramStateAndRef.state.data is IndiaCommercialPaperProgram.State)
            return handshake
        else
            throw IndiaCPException("Unexpected '${cpProgramStateAndRef.state.data.javaClass.simpleName}' Payload Received in ${this.javaClass.simpleName} Flow Logic. Expected: ${IndiaCommercialPaperProgram.State::class.java.name}", Error.SourceEnum.DL_R3CORDA)
    }

    override fun assembleSharedTX(handshake: TwoPartyDealFlow.Handshake<IndiaCPDocumentPayload>): Pair<TransactionBuilder, List<CompositeKey>> {
        val cpProgramStateAndRef = handshake.payload.getCPProgramStateAndRef()
        val cpIssues : List<StateAndRef<IndiaCommercialPaper.State>> = CPUtils.getAllCP(serviceHub, cpProgramStateAndRef.state.data.programId)

        val tx = IndiaCommercialPaperProgram().generateTransactionWithISIN(cpProgramStateAndRef, cpIssues, handshake.payload.notary)

        // And add a request for timestamping
        tx.setTime(serviceHub.clock.instant(), 30.seconds)

        return Pair(tx, arrayListOf(cpProgramStateAndRef.state.data.issuer.owningKey))
    }
}

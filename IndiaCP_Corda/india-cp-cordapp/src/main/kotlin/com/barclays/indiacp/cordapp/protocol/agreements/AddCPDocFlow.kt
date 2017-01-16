package com.barclays.indiacp.cordapp.protocol.agreements

import co.paralleluniverse.fibers.Suspendable
import com.barclays.indiacp.cordapp.api.IndiaCPApi
import com.barclays.indiacp.cordapp.contract.IndiaCommercialPaper
import com.barclays.indiacp.cordapp.contract.IndiaCommercialPaperProgram
import com.barclays.indiacp.cordapp.utilities.CPUtils
import com.barclays.indiacp.cordapp.utilities.ErrorUtils
import com.barclays.indiacp.model.CPIssueError
import com.barclays.indiacp.model.Error
import com.barclays.indiacp.model.IndiaCPDocumentDetails
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
import java.time.Instant

/**
 * This is the Flow to manage the consensus between any 2 parties exchanging agreements as part of the India
 * Commercial Paper Issuance. These agreements are digitally signed documents that are verified as part of the
 * smart contract verification.
 *
 * Created by ritukedia
 */
class AddCPDocFlow(val contractStateAndRef: StateAndRef<IndiaCommercialPaper.State>, val docType: IndiaCPDocumentDetails.DocTypeEnum, val acceptor: Party) : FlowLogic<SignedTransaction>() {

    class Services(services: PluginServiceHub) : SingletonSerializeAsToken() {

        init {
            services.registerFlowInitiator(DocAttachmentUploadInitiator::class) { DocAttachmentUploadAcceptor(it) }
        }
    }

    companion object {

        object INITIATING_DOC_UPLOAD : ProgressTracker.Step("Sending Document") {
            override fun childProgressTracker(): ProgressTracker = TwoPartyDealFlow.Primary.tracker()
        }
        object RECEIVED_ACCEPTANCE : ProgressTracker.Step("Received Acceptance Response")

        fun tracker() = ProgressTracker(INITIATING_DOC_UPLOAD, RECEIVED_ACCEPTANCE)
    }

    override val progressTracker: ProgressTracker = tracker()

    val startingProgressStep: ProgressTracker.Step = INITIATING_DOC_UPLOAD

    @Suspendable
    override fun call(): SignedTransaction {
        require(serviceHub.networkMapCache.notaryNodes.isNotEmpty()) { "No notary nodes registered" }
        val notary = serviceHub.networkMapCache.notaryNodes.first().notaryIdentity
        val myKey = serviceHub.legalIdentityKey
        progressTracker.currentStep = startingProgressStep
        val instigator = DocAttachmentUploadInitiator(
                acceptor,
                IndiaCPPayload(contractStateAndRef, docType, notary),
                myKey,
                progressTracker.getChildProgressTracker(startingProgressStep)!!)
        val stx = subFlow(instigator)
        return stx
    }

}

/**
 * Initiator Side of the Flow for Initiating the CP Attachment Upload. Currently only the Deal Confirmation Document flow
 * uses this
 */
open class DocAttachmentUploadInitiator(override val otherParty: Party,
                                override val payload: IndiaCPPayload,
                                override val myKeyPair: KeyPair,
                                override val progressTracker: ProgressTracker = TwoPartyDealFlow.Primary.tracker()) : TwoPartyDealFlow.Primary() {

    override val notaryNode: NodeInfo get() =
    serviceHub.networkMapCache.notaryNodes.filter { it.notaryIdentity == payload.notary }.single()

}

/**
 * Acceptor Side of the Flow for Receiving the CP Attachment. Currently only the Deal Confirmation Document flow
 * uses this
 */
open class DocAttachmentUploadAcceptor(override val otherParty: Party,
                               override val progressTracker: ProgressTracker = TwoPartyDealFlow.Secondary.tracker()) : TwoPartyDealFlow.Secondary<IndiaCPPayload>() {

    override fun validateHandshake(handshake: TwoPartyDealFlow.Handshake<IndiaCPPayload>): TwoPartyDealFlow.Handshake<IndiaCPPayload> {
        val cpStateAndRef = handshake.payload.contractStateAndRef
        if (cpStateAndRef.state.data is IndiaCommercialPaper.State)
            return handshake
        else
            throw IndiaCPException("Unexpected '${cpStateAndRef.state.data.javaClass.simpleName}' Payload Received in ${this.javaClass.simpleName} Flow Logic. Expected: ${IndiaCommercialPaper.State::class.java.name}", Error.SourceEnum.DL_R3CORDA)
    }

    override fun assembleSharedTX(handshake: TwoPartyDealFlow.Handshake<IndiaCPPayload>): Pair<TransactionBuilder, List<CompositeKey>> {
        val cpStateAndRef = handshake.payload.getCPStateAndRef()
        val docType = handshake.payload.docType
        var tx : TransactionBuilder? = null
        var docHash : SecureHash? = null

        when (docType) {
            IndiaCPDocumentDetails.DocTypeEnum.DEAL_CONFIRMATION_DOC -> {
                tx = IndiaCommercialPaper().generateDealConfirmation(cpStateAndRef, handshake.payload.notary)
                docHash = CPUtils.getDocHashAndStatus(cpStateAndRef.state.data.dealConfirmationDocId).first
            }
            else -> {
                throw IndiaCPException(CPIssueError.DOC_UPLOAD_ERROR, Error.SourceEnum.DL_R3CORDA)
            }
        }

        //Resolve Attachment on the Depository Node
        if (docHash == null) {
            throw IndiaCPException(CPIssueError.DOC_UPLOAD_ERROR, Error.SourceEnum.DL_R3CORDA, "The ${docType} Document May Not have been Uploaded Correctly. The DocHash on the IndiaCommercialPaper Smart Contract was null.")
        }
        subFlow(FetchAttachmentsFlow(setOf(docHash!!), otherParty))

        // And add a request for timestamping
        tx!!.setTime(serviceHub.clock.instant(), 30.seconds)

        return Pair(tx, arrayListOf(serviceHub.myInfo.legalIdentity.owningKey))
    }
}

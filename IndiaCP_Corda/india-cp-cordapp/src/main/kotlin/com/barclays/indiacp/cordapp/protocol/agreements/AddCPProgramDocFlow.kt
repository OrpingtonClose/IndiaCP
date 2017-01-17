package com.barclays.indiacp.cordapp.protocol.agreements

import co.paralleluniverse.fibers.Suspendable
import com.barclays.indiacp.cordapp.contract.IndiaCommercialPaperProgram
import com.barclays.indiacp.cordapp.protocol.issuer.IssueCPProgramFlow
import com.barclays.indiacp.cordapp.utilities.CPUtils
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
import net.corda.core.utilities.ProgressTracker
import net.corda.flows.FetchAttachmentsFlow
import net.corda.flows.TwoPartyDealFlow
import java.security.KeyPair

/**
 * This is the Flow to upload the various documents/agreements between the participants of the CP Program.
 * These agreements mimic the current documents & verifications as mandated by the India CP regulations, which
 * in future may be obviated by the Smart Contract proof of the same on the Blockchain.
 * The Documents currently exchanged are:
 * Letter of Intent sent by Issuer to Depository (NSDL) to generate the ISIN
 * Collection of all documents sent by Issuer to IPA for verification as mandated by RBI:
 *      Deal Confirmation Documents with each Investor under the CP Program
 *      Credit Rating Document applied to this CP Program
 *      Board Resolution Document applied to this CP Program for Short Term Borrowing Limit Approval
 *      Other Company Financial Documents
 * IPA Certificate sent by IPA to Issuer as a proof of verification of all documents sent by Issuer
 * Corporate Action Form sent by Issuer to Depository to transfer CP to Investor's DP Account
 *
 * Created by ritukedia
 */
class AddCPProgramDocFlow(val contractStateAndRef: StateAndRef<IndiaCommercialPaperProgram.State>, val docType: IndiaCPDocumentDetails.DocTypeEnum, val initiator: Party, val acceptor: Party) : FlowLogic<SignedTransaction>() {

    class Services(services: PluginServiceHub) : SingletonSerializeAsToken() {

        init {
            services.registerFlowInitiator(CPProgramDocAttachmentUploadInitiator::class) { CPProgramDocAttachmentUploadAcceptor(it) }
        }
    }

    companion object {

        object INITIATING_DOC_UPLOAD : ProgressTracker.Step("Sending Document") {
            override fun childProgressTracker(): ProgressTracker = TwoPartyDealFlow.Primary.tracker()
        }
        object RECEIVED_ACCEPTANCE : ProgressTracker.Step("Received Acceptance Response")
        object COPYING_TO_PARTICIPANTS : ProgressTracker.Step("Propogating transaction to all participants")


        fun tracker() = ProgressTracker(INITIATING_DOC_UPLOAD, RECEIVED_ACCEPTANCE, COPYING_TO_PARTICIPANTS)
    }

    override val progressTracker: ProgressTracker = tracker()

    val startingProgressStep: ProgressTracker.Step = INITIATING_DOC_UPLOAD

    @Suspendable
    override fun call(): SignedTransaction {
        require(serviceHub.myInfo.legalIdentity.equals(initiator))
        require(serviceHub.networkMapCache.notaryNodes.isNotEmpty()) { "No notary nodes registered" }
        val notary = serviceHub.networkMapCache.notaryNodes.first().notaryIdentity
        progressTracker.currentStep = startingProgressStep
        val instigator = CPProgramDocAttachmentUploadInitiator(
                acceptor,
                IndiaCPDocumentPayload(contractStateAndRef, docType, notary),
                serviceHub.legalIdentityKey,
                progressTracker.getChildProgressTracker(startingProgressStep)!!)
        val stx = subFlow(instigator)

        progressTracker.currentStep = COPYING_TO_PARTICIPANTS
        val parties = contractStateAndRef.state.data.parties
        val initiator = serviceHub.myInfo.legalIdentity
        if (parties.isNotEmpty()) {
            // Copy the transaction to other participant nodes
            parties.filter{!it.equals(initiator)}.filter{!it.equals(acceptor)}.forEach { send(it, stx) }
        }
        return stx
    }

}

/**
 * Initiator Side of the Flow for any of the two party agreement calls
 * For e.g. In case of ISIN Request a Letter of Intent request is sent by the Issuer to the Depository
 * In this example the Issuer is the Request Initiator and the Depository is the Request Acceptor
 * The Request Acceptor validates the Payload and Creates the Agreed Shared Transaction
 */
open class CPProgramDocAttachmentUploadInitiator(override val otherParty: Party,
                                                 override val payload: IndiaCPDocumentPayload,
                                                 override val myKeyPair: KeyPair,
                                                 override val progressTracker: ProgressTracker = TwoPartyDealFlow.Primary.tracker()) : TwoPartyDealFlow.Primary() {

    override val notaryNode: NodeInfo get() =
    serviceHub.networkMapCache.notaryNodes.filter { it.notaryIdentity == payload.notary }.single()

}

/**
 * Depository Side of the Flow for Accepting the ISIN Request Documents. The actual ISIN will be assigned
 * in a separate flow. See the addISIN endpoint in IndiaCPProgramApi
 */
open class CPProgramDocAttachmentUploadAcceptor(override val otherParty: Party,
                                                override val progressTracker: ProgressTracker = TwoPartyDealFlow.Secondary.tracker()) : TwoPartyDealFlow.Secondary<IndiaCPDocumentPayload>() {

    override fun validateHandshake(handshake: TwoPartyDealFlow.Handshake<IndiaCPDocumentPayload>): TwoPartyDealFlow.Handshake<IndiaCPDocumentPayload> {
        val isinDocAgreement = handshake.payload
        val cpProgramStateAndRef = isinDocAgreement.contractStateAndRef
        if (cpProgramStateAndRef.state.data is IndiaCommercialPaperProgram.State) {
            return handshake
        }
        else
            throw IndiaCPException("Unexpected '${cpProgramStateAndRef.state.data.javaClass.simpleName}' Payload Received in ${this.javaClass.simpleName} Flow Logic. Expected: ${IndiaCommercialPaperProgram.State::class.java.name}", Error.SourceEnum.DL_R3CORDA)
    }

    override fun assembleSharedTX(handshake: TwoPartyDealFlow.Handshake<IndiaCPDocumentPayload>): Pair<TransactionBuilder, List<CompositeKey>> {
        val docType = handshake.payload.docType
        val cpProgramStateAndRef = handshake.payload.getCPProgramStateAndRef()
        var tx : TransactionBuilder? = null
        var docHash : SecureHash? = null

        when (docType) {
            IndiaCPDocumentDetails.DocTypeEnum.DEPOSITORY_DOCS -> {
                tx = IndiaCommercialPaperProgram().generateTransactionWithISINDocAttachment(cpProgramStateAndRef, handshake.payload.notary)
                docHash = CPUtils.getDocHashAndStatus(cpProgramStateAndRef.state.data.isinGenerationRequestDocId).first
            }
            IndiaCPDocumentDetails.DocTypeEnum.IPA_DOCS -> {
                tx = IndiaCommercialPaperProgram().generateTransactionWithIPADocAttachment(cpProgramStateAndRef, handshake.payload.notary)
                docHash = CPUtils.getDocHashAndStatus(cpProgramStateAndRef.state.data.ipaVerificationRequestDocId).first
            }
            IndiaCPDocumentDetails.DocTypeEnum.IPA_CERTIFICATE_DOC -> {
                tx = IndiaCommercialPaperProgram().generateTransactionWithIPACertificateDocAttachment(cpProgramStateAndRef, handshake.payload.notary)
                docHash = CPUtils.getDocHashAndStatus(cpProgramStateAndRef.state.data.ipaCertificateDocId).first
            }
            IndiaCPDocumentDetails.DocTypeEnum.CORPORATE_ACTION_FORM -> {
                tx = IndiaCommercialPaperProgram().generateTransactionWithCAFormDocAttachment(cpProgramStateAndRef, handshake.payload.notary)
                docHash = CPUtils.getDocHashAndStatus(cpProgramStateAndRef.state.data.corporateActionFormDocId).first
            }
            else -> {
                throw IndiaCPException(CPIssueError.DOC_UPLOAD_ERROR, Error.SourceEnum.DL_R3CORDA)
            }
        }

        //Resolve Attachment on the Acceptor Node
        if (docHash == null) {
            throw IndiaCPException(CPIssueError.DOC_UPLOAD_ERROR, Error.SourceEnum.DL_R3CORDA, "The ${docType} Document May Not have been Uploaded Correctly. The DocHash on the IndiaCommercialPaper Smart Contract was null.")
        }
        subFlow(FetchAttachmentsFlow(setOf(docHash!!), otherParty))

        // And add a request for timestamping
        tx!!.setTime(serviceHub.clock.instant(), 30.seconds)

        // Signing the tx with the key of the acceptor node, i.e. this node
        return Pair(tx, arrayListOf(serviceHub.myInfo.legalIdentity.owningKey))
    }

    companion object {

        object ACCEPTING_ISIN_AGREEMENT : ProgressTracker.Step("Accepting ISIN Generation Letter of Intent Doc") {
            override fun childProgressTracker(): ProgressTracker = TwoPartyDealFlow.Secondary.tracker()
        }

        fun tracker() = ProgressTracker(ACCEPTING_ISIN_AGREEMENT)

    }

    //TODO: If we pass this parent progress tracker then the child progress tracker i.e. TwoPartyDealFlow.Secondary.tracker()
    //steps are not being recognized
    //override val progressTracker = CPProgramDocAttachmentUploadAcceptor.tracker()

}
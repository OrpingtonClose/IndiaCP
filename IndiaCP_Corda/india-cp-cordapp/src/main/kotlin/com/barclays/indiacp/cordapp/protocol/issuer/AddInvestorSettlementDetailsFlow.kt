package com.barclays.indiacp.cordapp.protocol.issuer

import co.paralleluniverse.fibers.Suspendable
import com.barclays.indiacp.cordapp.api.IndiaCPApi
import com.barclays.indiacp.cordapp.contract.IndiaCommercialPaper
import com.barclays.indiacp.cordapp.utilities.CPUtils
import net.corda.core.TransientProperty
import net.corda.core.contracts.StateAndRef
import net.corda.core.contracts.TransactionState
import net.corda.core.contracts.TransactionType
import net.corda.core.flows.FlowLogic
import net.corda.core.node.NodeInfo
import net.corda.core.seconds
import net.corda.core.transactions.SignedTransaction
import net.corda.core.utilities.ProgressTracker
import net.corda.flows.NotaryFlow
import java.security.KeyPair
import java.time.Instant

/**
 * This whole class is really part of a demo just to initiate the agreement of a deal with a simple
 * API call from a single party without bi-directional access to the database of offers etc.
 *
 * In the "real world", we'd probably have the offers sitting in the platform prior to the agreement step
 * or the protocol would have to reach out to external systems (or users) to verify the deals.
 */
class AddInvestorSettlementDetailsFlow(val cpRefId: String, val settlementDetails: IndiaCPApi.SettlementDetailsJSONObject) : FlowLogic<SignedTransaction>() {

    companion object {
        //val PROSPECTUS_HASH = SecureHash.parse("decd098666b9657314870e192ced0c3519c2c9d395507a238338f8d003929de9")

        object ADDING_INVESTOR_SETTLEMENT_DETAILS : ProgressTracker.Step("Adding Investor Settlement Details for the issued commercial paper")
        object OBTAINING_NOTARY_SIGNATURE : ProgressTracker.Step("Obtaining Notary Signature")
        object NOTARY_SIGNATURE_OBTAINED : ProgressTracker.Step("Notary Signature Obtained")
        object RECORDING_INVESTOR_SETTLEMENT_DETAILS : ProgressTracker.Step("Recording Investor Settlement Details in Local Storage")
        object INVESTOR_SETTLEMENT_DETAILS_RECORDED : ProgressTracker.Step("Investor Settlement Details Recorded in Local Storage")

        // We vend a progress tracker that already knows there's going to be a TwoPartyTradingProtocol involved at some
        // point: by setting up the tracker in advance, the user can see what's coming in more detail, instead of being
        // surprised when it appears as a new set of tasks below the current one.
        fun tracker() = ProgressTracker(ADDING_INVESTOR_SETTLEMENT_DETAILS, OBTAINING_NOTARY_SIGNATURE, NOTARY_SIGNATURE_OBTAINED, RECORDING_INVESTOR_SETTLEMENT_DETAILS, INVESTOR_SETTLEMENT_DETAILS_RECORDED)
    }

    override val progressTracker = tracker()

    @Suspendable
    override fun call(): SignedTransaction {
        var cpReference: StateAndRef<IndiaCommercialPaper.State> = CPUtils.getReferencedCommercialPaperStateRef(serviceHub, cpRefId)
//        val state = serviceHub.loadState(cpReference.ref) as TransactionState<IndiaCommercialPaper.State>
//        cpReference = StateAndRef(state, cpReference.ref)

        val investorSettlementDetails: IndiaCommercialPaper.SettlementDetails = IndiaCommercialPaper.SettlementDetails(
                paymentAccountDetails = IndiaCommercialPaper.PaymentAccountDetails(
                        creditorName = settlementDetails.paymentAccountDetailsJSONObject.creditorName,
                        bankAccountDetails = settlementDetails.paymentAccountDetailsJSONObject.bankAccountDetails,
                        bankName = settlementDetails.paymentAccountDetailsJSONObject.bankName,
                        rtgsCode = settlementDetails.paymentAccountDetailsJSONObject.rtgsCode
                ),
                depositoryAccountDetails = IndiaCommercialPaper.DepositoryAccountDetails (
                        dpName = settlementDetails.depositoryAccountDetailsJSONObject.dpName,
                        clientId =  settlementDetails.depositoryAccountDetailsJSONObject.clientId,
                        dpID =  settlementDetails.depositoryAccountDetailsJSONObject.dpID)
        )

        val notaryNode = serviceHub.networkMapCache.notaryNodes.filter { it.notaryIdentity == cpReference.state.notary }.single()

        progressTracker.currentStep = ADDING_INVESTOR_SETTLEMENT_DETAILS

        val ptx = TransactionType.General.Builder(notaryNode.notaryIdentity)

        val tx = IndiaCommercialPaper().addInvestorSettlementDetails(ptx, cpReference, investorSettlementDetails)

        // Attach the prospectus.
        //tx.addAttachment(serviceHub.storageService.attachments.openAttachment(PROSPECTUS_HASH)!!.id)

        // Requesting timestamping, all CP must be timestamped.
        tx.setTime(Instant.now(), 30.seconds)

        // Sign it as ourselves.
        tx.signWith(serviceHub.legalIdentityKey)

        // Get the notary to sign the timestamp
        progressTracker.currentStep = OBTAINING_NOTARY_SIGNATURE
        val notarySig = subFlow(NotaryFlow.Client(tx.toSignedTransaction(false)))
        progressTracker.currentStep = NOTARY_SIGNATURE_OBTAINED
        tx.addSignatureUnchecked(notarySig)

        // Commit it to local storage.
        val stx = tx.toSignedTransaction(true)
        progressTracker.currentStep = RECORDING_INVESTOR_SETTLEMENT_DETAILS
        serviceHub.recordTransactions(listOf(stx))
        progressTracker.currentStep = INVESTOR_SETTLEMENT_DETAILS_RECORDED

        return stx
    }
}


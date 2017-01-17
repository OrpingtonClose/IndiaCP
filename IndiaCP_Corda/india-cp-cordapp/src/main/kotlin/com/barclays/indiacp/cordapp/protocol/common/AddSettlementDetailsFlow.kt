package com.barclays.indiacp.cordapp.protocol.common

import co.paralleluniverse.fibers.Suspendable
import com.barclays.indiacp.cordapp.contract.IndiaCommercialPaper
import com.barclays.indiacp.cordapp.utilities.CPUtils
import com.barclays.indiacp.model.CPIssueError
import com.barclays.indiacp.model.Error
import com.barclays.indiacp.model.IndiaCPException
import net.corda.core.contracts.StateAndRef
import net.corda.core.flows.FlowLogic
import net.corda.core.node.NodeInfo
import net.corda.core.seconds
import net.corda.core.transactions.SignedTransaction
import net.corda.core.utilities.ProgressTracker
import net.corda.flows.NotaryFlow
import java.time.Instant

/**
 * This is the Flow Logic for Issuing Commercial Paper under a previously commenced Commercial Paper Program umbrella.
 */
class AddSettlementDetailsFlow(val cpStateAndRef: StateAndRef<IndiaCommercialPaper.State>, val settlementDetails: IndiaCommercialPaper.SettlementDetails) : FlowLogic<SignedTransaction>() {

    companion object {
        object AMENDING_SETTLEMENT_DETAILS : ProgressTracker.Step("Amending settlement details on the CP")
        object OBTAINING_NOTARY_SIGNATURE : ProgressTracker.Step("Obtaining Notary Signature")
        object NOTARY_SIGNATURE_OBTAINED : ProgressTracker.Step("Notary Signature Obtained")
        object RECORDING_TRANSACTION : ProgressTracker.Step("Recording Transaction in Local Storage")
        object TRANSACTION_RECORDED : ProgressTracker.Step("Transaction Recorded in Local Storage")
        object COPYING_TO_PARTICIPANTS : ProgressTracker.Step("Propogating transaction to all participants")

        fun tracker() = ProgressTracker(AMENDING_SETTLEMENT_DETAILS, OBTAINING_NOTARY_SIGNATURE, NOTARY_SIGNATURE_OBTAINED, RECORDING_TRANSACTION, TRANSACTION_RECORDED, COPYING_TO_PARTICIPANTS)
    }

    override val progressTracker = tracker()

    @Suspendable
    override fun call(): SignedTransaction {
        val notary: NodeInfo = serviceHub.networkMapCache.notaryNodes[0]
        val initiator = serviceHub.myInfo.legalIdentity

        progressTracker.currentStep = AMENDING_SETTLEMENT_DETAILS

        val tx = IndiaCommercialPaper().generateAddSettlementDetails(cpStateAndRef, settlementDetails, initiator, notary = notary.notaryIdentity)

        // Requesting timestamping, all CP must be timestamped.
        tx.setTime(Instant.now(), 30.seconds)

        // Sign it as an Initiator
        tx.signWith(serviceHub.legalIdentityKey)

        // Get the notary to sign the timestamp
        progressTracker.currentStep = OBTAINING_NOTARY_SIGNATURE
        val notarySig = subFlow(NotaryFlow.Client(tx.toSignedTransaction(false)))
        progressTracker.currentStep = NOTARY_SIGNATURE_OBTAINED
        tx.addSignatureUnchecked(notarySig)

        // Commit it to local storage.
        val stx = tx.toSignedTransaction(true)
        progressTracker.currentStep = RECORDING_TRANSACTION
        serviceHub.recordTransactions(listOf(stx))
        progressTracker.currentStep = TRANSACTION_RECORDED

        progressTracker.currentStep = COPYING_TO_PARTICIPANTS
        val parties = cpStateAndRef.state.data.parties
        if (parties.isNotEmpty()) {
            // Copy the transaction to other participant nodes
            parties.filter{!it.equals(initiator)}.forEach { send(it, stx) }
        }

        return stx
    }
}
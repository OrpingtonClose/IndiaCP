package com.barclays.indiacp.cordapp.protocol.issuer

import co.paralleluniverse.fibers.Suspendable
import com.barclays.indiacp.cordapp.contract.IndiaCommercialPaper
import com.barclays.indiacp.cordapp.utilities.CPUtils
import com.barclays.indiacp.model.CPIssueError
import com.barclays.indiacp.model.Error
import com.barclays.indiacp.model.IndiaCPException
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
class IssueCPFlow(val contractState: IndiaCommercialPaper.State) : FlowLogic<SignedTransaction>() {

    companion object {
        object PROGRAM_CEILING_CHECK : ProgressTracker.Step("Checking Program Ceiling Amount against Outstanding Issues")
        object SELF_ISSUING : ProgressTracker.Step("Issuing and timestamping some commercial paper")
        object OBTAINING_NOTARY_SIGNATURE : ProgressTracker.Step("Obtaining Notary Signature")
        object NOTARY_SIGNATURE_OBTAINED : ProgressTracker.Step("Notary Signature Obtained")
        object RECORDING_TRANSACTION : ProgressTracker.Step("Recording Transaction in Local Storage")
        object TRANSACTION_RECORDED : ProgressTracker.Step("Transaction Recorded in Local Storage")
        object COPYING_TO_PARTICIPANTS : ProgressTracker.Step("Propogating transaction to all participants")

        fun tracker() = ProgressTracker(PROGRAM_CEILING_CHECK, SELF_ISSUING, OBTAINING_NOTARY_SIGNATURE, NOTARY_SIGNATURE_OBTAINED, RECORDING_TRANSACTION, TRANSACTION_RECORDED, COPYING_TO_PARTICIPANTS)
    }

    override val progressTracker = tracker()

    @Suspendable
    override fun call(): SignedTransaction {
        val notary: NodeInfo = serviceHub.networkMapCache.notaryNodes[0]

        progressTracker.currentStep = PROGRAM_CEILING_CHECK
        val cpProgramStateRef = CPUtils.getCPProgramStateRefNonNull(serviceHub, contractState.cpProgramID)
        //Perform limit check
        if ((cpProgramStateRef.state.data.programAllocatedValue!!.quantity + contractState.faceValue.quantity) > (cpProgramStateRef.state.data.programSize.quantity)) {
            throw IndiaCPException(CPIssueError.PROGRAM_CEILING_EXCEEDED_ERROR, Error.SourceEnum.DL_R3CORDA, "This Program Cannot be Initiated. The Face Value of this CP ${contractState.faceValue} exceeds the available allocation limit as restricted by the program ceiling ${cpProgramStateRef.state.data.programSize}.")
        }

        progressTracker.currentStep = SELF_ISSUING

        val tx = IndiaCommercialPaper().generateIssue(contractState, cpProgramStateRef = cpProgramStateRef, notary = notary.notaryIdentity)

        // Requesting timestamping, all CP must be timestamped.
        tx.setTime(Instant.now(), 30.seconds)

        // Sign it as Issuer.
        val issuerNode = serviceHub.myInfo
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
        val parties = contractState.parties
        if (parties.isNotEmpty()) {
            // Copy the transaction to other participant nodes
            parties.filter{!it.equals(issuerNode.legalIdentity)}.forEach { send(it, stx) }
        }

        return stx
    }
}
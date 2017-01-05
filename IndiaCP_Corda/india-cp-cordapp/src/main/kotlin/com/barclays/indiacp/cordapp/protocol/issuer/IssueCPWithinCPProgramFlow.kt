package com.barclays.indiacp.cordapp.protocol.issuer

import co.paralleluniverse.fibers.Suspendable
import com.barclays.indiacp.cordapp.contract.IndiaCommercialPaperProgram
import com.barclays.indiacp.cordapp.utilities.CP_PROGRAM_FLOW_STAGES
import com.barclays.indiacp.model.IndiaCPIssue
import net.corda.contracts.asset.DUMMY_CASH_ISSUER
import net.corda.core.contracts.*
import net.corda.core.crypto.Party
import net.corda.core.crypto.SecureHash
import net.corda.core.flows.FlowLogic
import net.corda.core.node.NodeInfo
import net.corda.core.node.services.linearHeadsOfType
import net.corda.core.seconds
import net.corda.core.transactions.SignedTransaction
import net.corda.core.utilities.ProgressTracker
import net.corda.flows.NotaryFlow
import java.time.Instant
import java.util.*

/**
 * This whole class is really part of a demo just to initiate the agreement of a deal with a simple
 * API call from a single party without bi-directional access to the database of offers etc.
 *
 * In the "real world", we'd probably have the offers sitting in the platform prior to the agreement step
 * or the protocol would have to reach out to external systems (or users) to verify the deals.
 */
class IssueCPWithinCPProgramFlow(val newCP: IndiaCPIssue) : FlowLogic<SignedTransaction>() {

    companion object {
        val PROSPECTUS_HASH = SecureHash.parse("decd098666b9657314870e192ced0c3519c2c9d395507a238338f8d003929de9")

        object CP_PROGRAM_ISSUE_CP : ProgressTracker.Step("Issuing a IndiaCP within a IndiaCP Program")
        object SELF_ISSUING : ProgressTracker.Step("Issuing CP and timestamping some commercial paper")
        object OBTAINING_NOTARY_SIGNATURE : ProgressTracker.Step("Obtaining Notary Signature for CP")
        object NOTARY_SIGNATURE_OBTAINED : ProgressTracker.Step("Notary Signature Obtained for CP")
        object RECORDING_TRANSACTION : ProgressTracker.Step("Recording Transaction in Local Storage for CP")
        object TRANSACTION_RECORDED : ProgressTracker.Step("Transaction Recorded in Local Storage for CP")

        // We vend a progress tracker that already knows there's going to be a TwoPartyTradingProtocol involved at some
        // point: by setting up the tracker in advance, the user can see what's coming in more detail, instead of being
        // surprised when it appears as a new set of tasks below the current one.
        fun tracker() = ProgressTracker(CP_PROGRAM_ISSUE_CP, SELF_ISSUING, OBTAINING_NOTARY_SIGNATURE, NOTARY_SIGNATURE_OBTAINED, RECORDING_TRANSACTION, TRANSACTION_RECORDED)
    }

    override val progressTracker = tracker()

    @Suspendable
    override fun call(): SignedTransaction {
        progressTracker.currentStep = CP_PROGRAM_ISSUE_CP

        val notary: NodeInfo = serviceHub.networkMapCache.notaryNodes[0]
        val issuer = getPartyByName(newCP.issuerId)
        val beneficiary = getPartyByName(newCP.beneficiaryId)
        val ipa = getPartyByName(newCP.ipaId)
        val depository = getPartyByName(newCP.depositoryId)

        val indiaCPProgramSF: StateAndRef<IndiaCommercialPaperProgram.State> = getCPProgramStateandRef(newCP.cpProgramId)

        val newProgAllowValue: Amount<Issued<Currency>> = indiaCPProgramSF.state.data.programAllocatedValue!!.plus((newCP.facevaluePerUnit * newCP.noOfUnits).DOLLARS `issued by` DUMMY_CASH_ISSUER);

        if(newProgAllowValue.quantity > indiaCPProgramSF.state.data.programSize.quantity)
        {
            println("Unable to Issue CP as CP Program as Insufficient remaining Balance.")
            throw InsufficientBalanceException(newProgAllowValue.minus(indiaCPProgramSF.state.data.programSize))
        }

        println("GOT ref id for program : " + indiaCPProgramSF.state.data.programId + ", having allocated amt of " + indiaCPProgramSF.state.data.programAllocatedValue
        + " and new allocated value will be " + newProgAllowValue)

        val tx = IndiaCommercialPaperProgram().createCPIssueWithinCPProgram(indiaCPProgramSF, issuer, beneficiary, ipa, depository, notary.notaryIdentity,newProgAllowValue, newCP, CP_PROGRAM_FLOW_STAGES.ISSUE_CP.endStatus)

        // Requesting timestamping, all CP must be timestamped.
        tx.setTime(Instant.now(), 30.seconds)

        // Sign it as Issuer.
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

        return stx
    }

    private fun getPartyByName(partyName: String) : Party {
        return serviceHub.networkMapCache.getNodeByLegalName(partyName)!!.legalIdentity
    }

    private fun getCPProgramStateandRef(ref: String): StateAndRef<IndiaCommercialPaperProgram.State>
    {

        val states = this.serviceHub.vaultService.linearHeadsOfType<IndiaCommercialPaperProgram.State>().filterValues { it.state.data.programId == ref }

        //For now, assumption is that you will always find a deal for updating the details.
        //If a deal is not found then we have a big problem :(
        val deals = states.values.map { it }
        return deals[0]
    }
}


package com.barclays.indiacp.cordapp.protocol.issuer

import co.paralleluniverse.fibers.Suspendable
import com.barclays.indiacp.cordapp.contract.OrgLevelBorrowProgram
import com.barclays.indiacp.cordapp.dto.OrgLevelProgramJSON
import net.corda.contracts.asset.DUMMY_CASH_ISSUER
import net.corda.core.contracts.*
import net.corda.core.crypto.Party
import net.corda.core.crypto.SecureHash
import net.corda.core.node.NodeInfo
import net.corda.core.flows.FlowLogic
import net.corda.core.node.services.linearHeadsOfType
import net.corda.core.seconds
import net.corda.core.transactions.SignedTransaction
import net.corda.core.utilities.ProgressTracker
import net.corda.flows.NotaryFlow
import java.time.Instant

/**
 * This whole class is really part of a demo just to initiate the agreement of a deal with a simple
 * API call from a single party without bi-directional access to the database of offers etc.
 *
 * In the "real world", we'd probably have the offers sitting in the platform prior to the agreement step
 * or the protocol would have to reach out to external systems (or users) to verify the deals.
 */
class OrgLevelBorrowProgramFlow(val orgProgram: OrgLevelProgramJSON) : FlowLogic<SignedTransaction>() {

    companion object {
        val PROSPECTUS_HASH = SecureHash.parse("decd098666b9657314870e192ced0c3519c2c9d395507a238338f8d003929de9")

        object ORG_PROGRAM_ISSUE : ProgressTracker.Step("Creating an ORG Level Borrowing Program")
        object SELF_ISSUING : ProgressTracker.Step("Issuing ORG Level Borrowing Program and timestamping")
        object OBTAINING_NOTARY_SIGNATURE : ProgressTracker.Step("Obtaining Notary Signature")
        object NOTARY_SIGNATURE_OBTAINED : ProgressTracker.Step("Notary Signature Obtained")
        object RECORDING_TRANSACTION : ProgressTracker.Step("Recording Transaction in Local Storage")
        object TRANSACTION_RECORDED : ProgressTracker.Step("Transaction Recorded in Local Storage")

        // We vend a progress tracker that already knows there's going to be a TwoPartyTradingProtocol involved at some
        // point: by setting up the tracker in advance, the user can see what's coming in more detail, instead of being
        // surprised when it appears as a new set of tasks below the current one.
        fun tracker() = ProgressTracker(ORG_PROGRAM_ISSUE, SELF_ISSUING, OBTAINING_NOTARY_SIGNATURE, NOTARY_SIGNATURE_OBTAINED, RECORDING_TRANSACTION, TRANSACTION_RECORDED)
    }

    override val progressTracker = tracker()

    @Suspendable
    override fun call(): SignedTransaction {
        progressTracker.currentStep = ORG_PROGRAM_ISSUE

        val notary: NodeInfo = serviceHub.networkMapCache.notaryNodes[0]
        val issuer = getPartyByName(orgProgram.issuer)



        val tx = OrgLevelBorrowProgram().generateIssue(
                OrgLevelBorrowProgram.OrgState(issuer, orgProgram.org_id, orgProgram.name,

                        orgProgram.purpose,

                        orgProgram.commencement_date.toInstant(),

                        orgProgram.borrowing_limit.DOLLARS `issued by` DUMMY_CASH_ISSUER,

                        orgProgram.borrowed_value.DOLLARS `issued by` DUMMY_CASH_ISSUER,

                        orgProgram.program_currency,

                        status="ORG_PROGRAM_ISSUED",

                        modifiedBy = orgProgram.user_id,

                        lastModified = Instant.now(), version = Integer(0)
                ), notary.notaryIdentity)




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

    private fun getCPProgramStateandRef(ref: String): StateAndRef<OrgLevelBorrowProgram.OrgState>
    {

        val states = this.serviceHub.vaultService.linearHeadsOfType<OrgLevelBorrowProgram.OrgState>().filterValues { it.state.data.orgId == ref }

        //For now, assumption is that you will always find a deal for updating the details.
        //If a deal is not found then we have a big problem :(
        val deals = states.values.map { it }
        return deals[0]
    }
}


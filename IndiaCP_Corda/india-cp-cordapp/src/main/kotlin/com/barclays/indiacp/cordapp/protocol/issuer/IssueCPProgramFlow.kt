package com.barclays.indiacp.cordapp.protocol.issuer

import co.paralleluniverse.fibers.Suspendable
import com.barclays.indiacp.cordapp.api.BorrowingLimitBoardResolutionApi
import com.barclays.indiacp.cordapp.api.CreditRatingApi
import com.barclays.indiacp.cordapp.api.IndiaCPProgramApi
import com.barclays.indiacp.cordapp.contract.BorrowingLimitBoardResolution
import com.barclays.indiacp.cordapp.contract.CreditRating
import com.barclays.indiacp.cordapp.contract.IndiaCommercialPaperProgram
import com.barclays.indiacp.cordapp.utilities.CPUtils
import com.barclays.indiacp.model.*
import net.corda.core.contracts.Amount
import net.corda.core.contracts.DOLLARS
import net.corda.core.contracts.StateAndRef
import net.corda.core.crypto.Party
import net.corda.core.flows.FlowLogic
import net.corda.core.node.NodeInfo
import net.corda.core.node.services.linearHeadsOfType
import net.corda.core.seconds
import net.corda.core.transactions.SignedTransaction
import net.corda.core.transactions.TransactionBuilder
import net.corda.core.utilities.ProgressTracker
import net.corda.flows.NotaryFlow
import java.time.Instant
import java.util.*

/**
 *
 */
class IssueCPProgramFlow(val contractState: IndiaCommercialPaperProgram.State) : FlowLogic<SignedTransaction>() {

    companion object {
        object CREDIT_LIMIT_CHECK : ProgressTracker.Step("Credit Limit Check")
        object CREDIT_VALIDITY_CHECK : ProgressTracker.Step("Credit Rating Validity Check")
        object BORROWING_LIMIT_CHECK : ProgressTracker.Step("Board Resolution Borrowing Limit Check")
        object SELF_ISSUING : ProgressTracker.Step("Self Issuing CP Program")
        object OBTAINING_NOTARY_SIGNATURE : ProgressTracker.Step("Obtaining Notary Signature")
        object NOTARY_SIGNATURE_OBTAINED : ProgressTracker.Step("Notary Signature Obtained")
        object RECORDING_TRANSACTION : ProgressTracker.Step("Recording Transaction in Local Storage")
        object TRANSACTION_RECORDED : ProgressTracker.Step("Transaction Recorded in Local Storage")
    }

    override val progressTracker = getTracker()

    fun getTracker(): ProgressTracker {
        return ProgressTracker(CREDIT_LIMIT_CHECK, CREDIT_VALIDITY_CHECK, BORROWING_LIMIT_CHECK, SELF_ISSUING, OBTAINING_NOTARY_SIGNATURE, NOTARY_SIGNATURE_OBTAINED, RECORDING_TRANSACTION, TRANSACTION_RECORDED)
    }

    @Suspendable
    override fun call(): SignedTransaction {
        val notary: NodeInfo = serviceHub.networkMapCache.notaryNodes[0]

        progressTracker.currentStep = CREDIT_LIMIT_CHECK

        //Checking that the netted allocated amount across all current "Active" CP Programs does not exceed the approved Credit Limit
        //Note this assumes that the current Active CP Programs are only the ones that have been created on this DL
        //It will not account for any CP Programs outside of the DL

        //Net the outstanding ProgramSizes
        val pairOfOpenAndSettledCPProgramSizes : Pair <Amount<Currency>, Amount<Currency>> = netCPPrograms()
        val netOpenCPProgramSize = pairOfOpenAndSettledCPProgramSizes.first
        val netSettledCPProgramSize = pairOfOpenAndSettledCPProgramSizes.second
        val netAllocatedProgramSize = netOpenCPProgramSize.quantity + netSettledCPProgramSize.quantity

        //Fetch the limit approved by the active credit rating document
        val creditRatingStateAndRef: StateAndRef<CreditRating.State>
        val creditRatingStates = serviceHub.vaultService.linearHeadsOfType<CreditRating.State>()
        if (creditRatingStates == null || creditRatingStates.isEmpty()) {
            throw IndiaCPException(CreditRatingError.DOES_NOT_EXIST_ERROR, Error.SourceEnum.DL_R3CORDA, "Credit Rating Document for this Legal Entity Not Found. The Legal Entity Should be setup with the Credit Rating and Board Resolution Documents before Initiating a CP Program.")
        } else {
            creditRatingStateAndRef = creditRatingStates!!.values.map { it }.first()
        }

        val creditRatingState = creditRatingStateAndRef!!.state!!.data
        val creditRatingLimit = creditRatingState!!.creditRatingAmount

        //verify that the the outstanding ProgramSizes match the currentOutstandingCreditBorrowing amount on the CreditRating and BorrowingLimitBoardResolution Contracts
        if (netAllocatedProgramSize != creditRatingState.currentOutstandingCreditBorrowing!!.quantity) {
            throw IndiaCPException(CPProgramError.CREATION_ERROR, Error.SourceEnum.UNKNOWN_SOURCE, "SEVERE ERROR: Netting of Open Positions mismathced with Credit Rating Document Current Borrowed Amount")
        }

        //Perform limit check
        if ((netAllocatedProgramSize + contractState.programSize.quantity) > (creditRatingLimit.quantity)) {
            throw IndiaCPException(CPProgramError.CREDIT_LIMIT_EXCEEDED_ERROR, Error.SourceEnum.DL_R3CORDA, "This Program Cannot be Initiated. The Total Program Size of this Program exceeds the net credit rating approved for this legal entity.")
        }

        progressTracker.currentStep = CREDIT_VALIDITY_CHECK

        //Checking that the maturity date of the CP Program is within the approved credit rating validity period
        if(!contractState.issueCommencementDate.isAfter(creditRatingState.creditRatingEffectiveDate.toInstant())
        || !contractState.maturityDate.isBefore(creditRatingState.creditRatingExpiryDate.toInstant()) ) {
            throw IndiaCPException(CPProgramError.CREDIT_RATING_VALIDITY_EXPIRED_ERROR, Error.SourceEnum.DL_R3CORDA, "This Program Cannot be Initiated. The Program Commencement Date and Maturity Date do not fall within the window for which the Credit Rating is valid.\n Credit Rating Commencement Date: ${creditRatingState.creditRatingEffectiveDate}, Credit Rating Expiry Date: ${creditRatingState.creditRatingExpiryDate}\n CPProgram Commencement Date: ${contractState.issueCommencementDate}, CPProgram Maturity Date: ${contractState.maturityDate}")
        }

        progressTracker.currentStep = BORROWING_LIMIT_CHECK

        //Fetch the short term borrowing limit approved by the board resolution
        val boardResolutionStateAndRef: StateAndRef<BorrowingLimitBoardResolution.State>
        val boardResolutionStates = serviceHub.vaultService.linearHeadsOfType<BorrowingLimitBoardResolution.State>()
        if (boardResolutionStates == null || boardResolutionStates.isEmpty()) {
            throw IndiaCPException(BoardResolutionError.DOES_NOT_EXIST_ERROR, Error.SourceEnum.DL_R3CORDA, "Board Resolution Document for this Legal Entity Not Found. The Legal Entity Should be setup with the Credit Rating and Board Resolution Documents before Initiating a CP Program.")
        } else {
            boardResolutionStateAndRef = boardResolutionStates!!.values.map{it}.first()
        }
        val boardResolutionState = boardResolutionStateAndRef!!.state.data
        val boardResolutionBorrowingLimit = boardResolutionState!!.boardResolutionBorrowingLimit

        //verify that the the outstanding ProgramSizes match the currentOutstandingCreditBorrowing amount on the CreditRating and BorrowingLimitBoardResolution Contracts
        if (netAllocatedProgramSize != boardResolutionState.currentOutstandingCreditBorrowing!!.quantity) {
            throw IndiaCPException(CPProgramError.CREATION_ERROR, Error.SourceEnum.UNKNOWN_SOURCE, "SEVERE ERROR: Netting of Open Positions mismathced with Board Resolution Document Current Borrowed Amount")
        }

        //Perform limit check
        if ((netAllocatedProgramSize + contractState.programSize.quantity) > (boardResolutionBorrowingLimit.quantity)) {
            throw IndiaCPException(CPProgramError.BORROWING_LIMIT_EXCEEDED_ERROR, Error.SourceEnum.DL_R3CORDA, "This Program Cannot be Initiated. The Total Program Size of this Program exceeds the net Borrowing Limit approved by the Board Resolution for this legal entity.")
        }

        progressTracker.currentStep = SELF_ISSUING

        val tx = IndiaCommercialPaperProgram().generateIssue(contractState, creditRatingStateAndRef!!, boardResolutionStateAndRef!!, notary = notary.notaryIdentity)

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

    private fun netCPPrograms() : Pair <Amount<Currency>, Amount<Currency>> {
        val cpPrograms = IndiaCPProgramApi(serviceHub).getAllCPProgram()
        val netProgramSize = cpPrograms?.sumByDouble { it.programSize.quantity.toDouble() }?.toLong()

        //todo: Return a pair of net Open Positions which are not yet Settled and net Settled Positions which are yet to mature
        return Pair<Amount<Currency>, Amount<Currency>>(Amount(netProgramSize ?: 0,Currency.getInstance("INR")), Amount(0,Currency.getInstance("INR")))
    }

    private fun getPartyByName(partyName: String) : Party {
        return serviceHub.networkMapCache.getNodeByLegalName(partyName)!!.legalIdentity
    }

    private fun getNodeByName(party: Party) : NodeInfo? {
        return serviceHub.networkMapCache.getNodeByLegalName(party.name)
    }
}


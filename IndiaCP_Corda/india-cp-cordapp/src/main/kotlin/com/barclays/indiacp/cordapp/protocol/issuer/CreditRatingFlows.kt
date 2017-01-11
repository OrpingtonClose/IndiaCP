package com.barclays.indiacp.cordapp.protocol.issuer

import co.paralleluniverse.fibers.Suspendable
import com.barclays.indiacp.cordapp.contract.CreditRating
import com.barclays.indiacp.cordapp.contract.LegalEntityDocumentContract
import com.barclays.indiacp.cordapp.contract.LegalEntityDocumentOwnableState
import com.barclays.indiacp.cordapp.utilities.ModelUtils
import com.barclays.indiacp.model.CreditRatingError
import com.barclays.indiacp.model.Error
import com.barclays.indiacp.model.IndiaCPException
import net.corda.core.contracts.OwnableState
import net.corda.core.crypto.SecureHash
import net.corda.core.flows.FlowLogic
import net.corda.core.node.NodeInfo
import net.corda.core.seconds
import net.corda.core.transactions.SignedTransaction
import net.corda.core.transactions.TransactionBuilder
import net.corda.core.utilities.ProgressTracker
import net.corda.flows.NotaryFlow
import java.time.Instant

/*
 * Flows for Setting up DL Stamped Immutable Credit Rating and Board Resolution Documents on DL.
 * The hash of the Documents is stamped on the DL as part of the corresponding Smart Contract State
 *
 * Created by ritukedia on 07/01/17.
 */
open class CreditRatingFlows(val contractState: CreditRating.State?, val command: String) : FlowLogic<SignedTransaction>() {

    companion object {
        object PRE_CHECKS : ProgressTracker.Step("Checking Status before Issuing/Amending Document for this Legal Entity")
        object BEGINNING_AMEND : ProgressTracker.Step("Amending Document for this Legal Entity")
        object SELF_ISSUING : ProgressTracker.Step("Issuing and timestamping Document for this Legal Entity")
        object CANCELLING : ProgressTracker.Step("Cancelling Document for this Legal Entity")
        object OBTAINING_NOTARY_SIGNATURE : ProgressTracker.Step("Obtaining Notary Signature")
        object NOTARY_SIGNATURE_OBTAINED : ProgressTracker.Step("Notary Signature Obtained")
        object RECORDING_TRANSACTION : ProgressTracker.Step("Recording Transaction in Local Storage")
        object TRANSACTION_RECORDED : ProgressTracker.Step("Transaction Recorded in Local Storage")
    }

    override val progressTracker = getTracker(command)

    fun getTracker(command: String): ProgressTracker {

        when (command) {
            LegalEntityDocumentContract.Commands.Issue::class.java.simpleName -> return ProgressTracker(PRE_CHECKS, SELF_ISSUING, OBTAINING_NOTARY_SIGNATURE, NOTARY_SIGNATURE_OBTAINED, RECORDING_TRANSACTION, TRANSACTION_RECORDED)
            LegalEntityDocumentContract.Commands.Amend::class.java.simpleName -> return ProgressTracker(PRE_CHECKS, BEGINNING_AMEND, OBTAINING_NOTARY_SIGNATURE, NOTARY_SIGNATURE_OBTAINED, RECORDING_TRANSACTION, TRANSACTION_RECORDED)
            LegalEntityDocumentContract.Commands.Cancel::class.java.simpleName -> return ProgressTracker(PRE_CHECKS, CANCELLING, OBTAINING_NOTARY_SIGNATURE, NOTARY_SIGNATURE_OBTAINED, RECORDING_TRANSACTION, TRANSACTION_RECORDED)
            else -> return ProgressTracker(PRE_CHECKS, SELF_ISSUING, OBTAINING_NOTARY_SIGNATURE, NOTARY_SIGNATURE_OBTAINED, RECORDING_TRANSACTION, TRANSACTION_RECORDED)
        }
    }

    @Suspendable
    override fun call(): SignedTransaction {
        progressTracker.currentStep = PRE_CHECKS

        val notary: NodeInfo = serviceHub.networkMapCache.notaryNodes[0]
        val ownerKey = serviceHub.legalIdentityKey
        var tx: TransactionBuilder? = null

        //check that there is only one Active Credit Rating Document
        val existingDocStates = serviceHub.vaultService.currentVault.statesOfType<CreditRating.State>()
        val activeDocStates = existingDocStates?.filter { it.state.data.status.equals(ModelUtils.DocumentStatus.ACTIVE.name) }
        if ( activeDocStates != null &&  activeDocStates?.isNotEmpty())
        {
            when (command) {
                LegalEntityDocumentContract.Commands.Issue::class.java.simpleName -> throw IndiaCPException(CreditRatingError.CREATION_ERROR, Error.SourceEnum.DL_R3CORDA, "An ACTIVE Credit Rating Document Issued for ${contractState!!.issuer} already exists. Only one Credit Rating document can be active at a given time. To amend the current active document please use the amendCreditRating REST endpoint")

                LegalEntityDocumentContract.Commands.Amend::class.java.simpleName -> {
                    //Amendment
                    progressTracker.currentStep = BEGINNING_AMEND
                    tx = CreditRating().generateAmend( activeDocStates!!.last(), contractState!!, notary.notaryIdentity)
                }

                LegalEntityDocumentContract.Commands.Cancel::class.java.simpleName -> {
                    //Cancelling
                    progressTracker.currentStep = CANCELLING
                    tx = CreditRating().generateCancel( activeDocStates!!.last(), notary.notaryIdentity)
                }
            }

        } else if (command.equals(LegalEntityDocumentContract.Commands.Issue::class.java.simpleName)) {

            //First Time Issue
            progressTracker.currentStep = SELF_ISSUING
            tx = CreditRating().generateIssue(contractState!!, notary.notaryIdentity)

        } else if (command.equals(LegalEntityDocumentContract.Commands.Cancel::class.java.simpleName) || command.equals(LegalEntityDocumentContract.Commands.Amend::class.java.simpleName)) {

            throw IndiaCPException(CreditRatingError.CANCELLATION_ERROR, Error.SourceEnum.DL_R3CORDA, "$command Action cannot be performed. No Active Document/Smart Contract Found For this Legal Entity.")
        }

        if (command.equals(LegalEntityDocumentContract.Commands.Issue::class.java.simpleName) || command.equals(LegalEntityDocumentContract.Commands.Amend::class.java.simpleName)) {
            // Attach the prospectus of the credit rating document uploaded with this Smart Contract
            val PROSPECTUS_HASH = SecureHash.parse(contractState!!.docHash)
            tx!!.addAttachment(serviceHub.storageService.attachments.openAttachment(PROSPECTUS_HASH)!!.id)
        }

        // Requesting timestamping, all CP must be timestamped.
        tx!!.setTime(Instant.now(), 30.seconds)

        // Sign it as ourselves.
        tx!!.signWith(ownerKey)

        // Get the notary to sign the timestamp
        progressTracker.currentStep = OBTAINING_NOTARY_SIGNATURE
        val notarySig = subFlow(NotaryFlow.Client(tx.toSignedTransaction(false)))
        progressTracker.currentStep = NOTARY_SIGNATURE_OBTAINED
        tx!!.addSignatureUnchecked(notarySig)

        // Commit it to local storage.
        val stx = tx.toSignedTransaction(true)
        progressTracker.currentStep = RECORDING_TRANSACTION
        serviceHub.recordTransactions(listOf(stx))
        progressTracker.currentStep = TRANSACTION_RECORDED

        return stx
    }
}


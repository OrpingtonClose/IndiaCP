package com.barclays.indiacp.cordapp.protocol.issuer

import co.paralleluniverse.fibers.Suspendable
import net.corda.core.crypto.Party
import net.corda.core.flows.FlowLogic
import net.corda.core.node.NodeInfo
import net.corda.core.node.PluginServiceHub
import net.corda.core.serialization.SingletonSerializeAsToken
import net.corda.core.transactions.SignedTransaction
import net.corda.core.transactions.WireTransaction
import net.corda.core.utilities.ProgressTracker

/**
 * Generic Acceptor Side of the Flow for Receiving the propogated fully signed transaction of CP or CP Program
 */
class OtherParticipantsTransactionPropogationFlow(val otherParty: Party) : FlowLogic<Unit>() {
    companion object {
        object RECEIVING : ProgressTracker.Step("Receiving fully signed transaction")
        object VERIFYING : ProgressTracker.Step("Verifying the fully signed transaction")
        object RECORDING_TRANSACTION : ProgressTracker.Step("Recording Transaction in Local Storage")
        object TRANSACTION_RECORDED : ProgressTracker.Step("Transaction Recorded in Local Storage")

        fun tracker() = ProgressTracker(RECEIVING, VERIFYING, RECORDING_TRANSACTION, TRANSACTION_RECORDED)
    }

    override val progressTracker = tracker()

    class Services(services: PluginServiceHub) : SingletonSerializeAsToken() {

        init {
            services.registerFlowInitiator(IssueCPFlow::class) { OtherParticipantsTransactionPropogationFlow(it) }
            services.registerFlowInitiator(IssueCPProgramFlow::class) { OtherParticipantsTransactionPropogationFlow(it) }
        }
    }

    @Suspendable
    override fun call() {

        progressTracker.currentStep = RECEIVING
        val receivedTransaction = receive<SignedTransaction>(otherParty)

        progressTracker.currentStep = VERIFYING

        val stx = receivedTransaction.unwrap<SignedTransaction> { validateAndRecordTransaction(it) }
    }

    fun validateAndRecordTransaction(stx: SignedTransaction): SignedTransaction {
        val notary: NodeInfo = serviceHub.networkMapCache.notaryNodes[0]
        val wtx: WireTransaction = stx.verifySignatures(otherParty.owningKey, notary.notaryIdentity.owningKey)

        //TODO: Create a wrapper payload object that accepts only IndiaCommercialPaper or IndiaCommercialPaperProgram
        // transaction states. For the purpose of demo the sending party signature verification is considered
        // sufficient proof of authenticity of the sender and the transaction payload
        //val command = wtx.commands.filter { it.value is IndiaCommercialPaper.Commands.Issue }.single()
        //val cpIssueState = wtx.outputs.filter { it.data is IndiaCommercialPaper.State }.single()

        //if (!cpIssueState.data.participants.contains(serviceHub.legalIdentityKey.public.composite))
        //throw IndiaCPException("Unexpected '${wtx}' Payload Received from ${otherParty}. This node ${serviceHub.myInfo} is not the intended participant on this transaction.", Error.SourceEnum.DL_R3CORDA)

        // Commit it to local storage.
        progressTracker.currentStep = RECORDING_TRANSACTION
        serviceHub.recordTransactions(listOf(stx))
        progressTracker.currentStep = TRANSACTION_RECORDED

        return stx
    }


}


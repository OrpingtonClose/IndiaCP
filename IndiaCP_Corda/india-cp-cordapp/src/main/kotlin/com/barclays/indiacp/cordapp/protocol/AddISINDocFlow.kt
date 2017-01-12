package com.barclays.indiacp.cordapp.protocol

import co.paralleluniverse.fibers.Suspendable
import com.barclays.indiacp.cordapp.contract.IndiaCommercialPaperProgram
import com.barclays.indiacp.model.IndiaCPProgramStatusEnum
import net.corda.core.contracts.DealState
import net.corda.core.contracts.StateAndRef
import net.corda.core.crypto.CompositeKey
import net.corda.core.crypto.Party
import net.corda.core.flows.FlowLogic
import net.corda.core.node.CordaPluginRegistry
import net.corda.core.node.NodeInfo
import net.corda.core.node.PluginServiceHub
import net.corda.core.seconds
import net.corda.core.serialization.SingletonSerializeAsToken
import net.corda.core.transactions.SignedTransaction
import net.corda.core.transactions.TransactionBuilder
import net.corda.core.utilities.ProgressTracker
import net.corda.core.utilities.trace
import net.corda.flows.TwoPartyDealFlow
import net.corda.flows.TwoPartyDealFlow.Acceptor
import net.corda.flows.TwoPartyDealFlow.AutoOffer
import net.corda.flows.TwoPartyDealFlow.Instigator
import java.security.KeyPair

/**
 * This whole class is really part of a demo just to initiate the agreement of a deal with a simple
 * API call from a single party without bi-directional access to the database of offers etc.
 *
 * In the "real world", we'd probably have the offers sitting in the platform prior to the agreement step
 * or the flow would have to reach out to external systems (or users) to verify the deals.
 */
class AddISINDocFlow(val cpProgramStateAndRef: StateAndRef<IndiaCommercialPaperProgram.State>) : FlowLogic<SignedTransaction>() {

    val cpProgramState = cpProgramStateAndRef.state.data

    companion object {

        object INITIATING_ISIN_AGREEMENT : ProgressTracker.Step("Sending the ISIN Letter of Intent to NSDL") {
            override fun childProgressTracker(): ProgressTracker = TwoPartyDealFlow.Primary.tracker()
        }
        object RECEIVED_ACCEPTANCE : ProgressTracker.Step("Received Depository Response")

        // We vend a progress tracker that already knows there's going to be a TwoPartyTradingFlow involved at some
        // point: by setting up the tracker in advance, the user can see what's coming in more detail, instead of being
        // surprised when it appears as a new set of tasks below the current one.
        fun tracker() = ProgressTracker(INITIATING_ISIN_AGREEMENT, RECEIVED_ACCEPTANCE)
    }

    override val progressTracker = tracker()

    init {
        progressTracker.currentStep = INITIATING_ISIN_AGREEMENT
    }

    class ISINDocService(services: PluginServiceHub) : SingletonSerializeAsToken() {

        object ACCEPTING_ISIN_AGREEMENT : ProgressTracker.Step("Accepting ISIN Generation Letter of Intent Doc") {
            override fun childProgressTracker(): ProgressTracker = TwoPartyDealFlow.Secondary.tracker()
        }

        init {
            val tracker = ProgressTracker(ACCEPTING_ISIN_AGREEMENT)
            services.registerFlowInitiator(ISINRequestInitiator::class) { ISINRequestAcceptor(it) }
        }
    }

    @Suspendable
    override fun call(): SignedTransaction {
        require(serviceHub.networkMapCache.notaryNodes.isNotEmpty()) { "No notary nodes registered" }
        val notary = serviceHub.networkMapCache.notaryNodes.first().notaryIdentity
        val otherParty = cpProgramState.depository
        progressTracker.currentStep = INITIATING_ISIN_AGREEMENT
        val myKey = serviceHub.legalIdentityKey
        val instigator = ISINRequestInitiator(
                otherParty,
                GenerateISINDocAgreement(cpProgramStateAndRef, notary),
                myKey,
                progressTracker.getChildProgressTracker(INITIATING_ISIN_AGREEMENT)!!
        )
        val stx = subFlow(instigator)
        return stx
    }

}

/**
 * One side of the flow for inserting a pre-agreed deal.
 */
open class ISINRequestInitiator(override val otherParty: Party,
                      override val payload: GenerateISINDocAgreement,
                      override val myKeyPair: KeyPair,
                      override val progressTracker: ProgressTracker = TwoPartyDealFlow.Primary.tracker()) : TwoPartyDealFlow.Primary() {

    override val notaryNode: NodeInfo get() =
    serviceHub.networkMapCache.notaryNodes.filter { it.notaryIdentity == payload.notary }.single()
}

/**
 * One side of the flow for inserting a pre-agreed deal.
 */
open class ISINRequestAcceptor(override val otherParty: Party,
                    override val progressTracker: ProgressTracker = TwoPartyDealFlow.Secondary.tracker()) : TwoPartyDealFlow.Secondary<GenerateISINDocAgreement>() {

    override fun validateHandshake(handshake: TwoPartyDealFlow.Handshake<GenerateISINDocAgreement>): TwoPartyDealFlow.Handshake<GenerateISINDocAgreement> {
        val isinDocAgreement = handshake.payload
        val cpProgramStateAndRef = isinDocAgreement.cpProgramStateAndRef

        //TODO: verifiy the signature on the ISIN Letter of Intent Document

        return handshake.copy(payload = isinDocAgreement.copy(cpProgramStateAndRef = cpProgramStateAndRef))
    }

    override fun assembleSharedTX(handshake: TwoPartyDealFlow.Handshake<GenerateISINDocAgreement>): Pair<TransactionBuilder, List<CompositeKey>> {
        val cpProgramStateAndRef = handshake.payload.cpProgramStateAndRef
        val tx = IndiaCommercialPaperProgram().generateTransactionWithISINDocAttachment(cpProgramStateAndRef, handshake.payload.notary)

        // And add a request for timestamping: it may be that none of the contracts need this! But it can't hurt
        // to have one.
        tx.setTime(serviceHub.clock.instant(), 30.seconds)
        return Pair(tx, arrayListOf(cpProgramStateAndRef.state.data.depository.owningKey))
    }
}

data class GenerateISINDocAgreement(val cpProgramStateAndRef: StateAndRef<IndiaCommercialPaperProgram.State>, val notary: Party)
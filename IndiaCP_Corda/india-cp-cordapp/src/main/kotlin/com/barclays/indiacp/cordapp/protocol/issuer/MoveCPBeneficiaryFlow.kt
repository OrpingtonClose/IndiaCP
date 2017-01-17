package com.barclays.indiacp.cordapp.protocol.issuer

import co.paralleluniverse.fibers.Suspendable
import com.barclays.indiacp.cordapp.contract.IndiaCommercialPaper
import com.barclays.indiacp.cordapp.contract.IndiaCommercialPaperProgram
import com.barclays.indiacp.cordapp.protocol.agreements.IndiaCPDocumentPayload
import com.barclays.indiacp.cordapp.utilities.CPUtils
import com.barclays.indiacp.model.Error
import com.barclays.indiacp.model.IndiaCPDocumentDetails
import com.barclays.indiacp.model.IndiaCPException
import net.corda.core.contracts.LinearState
import net.corda.core.contracts.StateAndRef
import net.corda.core.crypto.CompositeKey
import net.corda.core.crypto.Party
import net.corda.core.flows.FlowLogic
import net.corda.core.node.NodeInfo
import net.corda.core.node.PluginServiceHub
import net.corda.core.seconds
import net.corda.core.serialization.SingletonSerializeAsToken
import net.corda.core.transactions.SignedTransaction
import net.corda.core.transactions.TransactionBuilder
import net.corda.core.utilities.ProgressTracker
import net.corda.flows.TwoPartyDealFlow
import java.security.KeyPair

/**
 * This is the Flow to transfer ownership of the CP from the Issuer to the Investor.
 *
 * Created by ritukedia
 */
class MoveCPBeneficiaryFlow(val contractStateAndRef: StateAndRef<IndiaCommercialPaper.State>) : FlowLogic<SignedTransaction>() {

    class Services(services: PluginServiceHub) : SingletonSerializeAsToken() {

        init {
            services.registerFlowInitiator(MoveCPBeneficiaryInitiator::class) { MoveCPBeneficiaryAcceptor(it) }
        }
    }

    companion object {

        object INITIATING_CP_BENEFICIARY_TRANSFER : ProgressTracker.Step("Initiating Transfer of Ownership of CP to Investor") {
            override fun childProgressTracker(): ProgressTracker = TwoPartyDealFlow.Primary.tracker()
        }
        object RECEIVED_ACCEPTANCE : ProgressTracker.Step("Received Investor Acceptance Response")
        object COPYING_TO_PARTICIPANTS : ProgressTracker.Step("Propogating transaction to all participants")

        fun tracker() = ProgressTracker(INITIATING_CP_BENEFICIARY_TRANSFER, RECEIVED_ACCEPTANCE, COPYING_TO_PARTICIPANTS)
    }

    override val progressTracker: ProgressTracker = tracker()

    val acceptorParty: Party = contractStateAndRef.state.data.investor

    val startingProgressStep: ProgressTracker.Step = INITIATING_CP_BENEFICIARY_TRANSFER

    @Suspendable
    override fun call(): SignedTransaction {
        require(serviceHub.networkMapCache.notaryNodes.isNotEmpty()) { "No notary nodes registered" }
        val notary = serviceHub.networkMapCache.notaryNodes.first().notaryIdentity
        val myKey = serviceHub.legalIdentityKey
        progressTracker.currentStep = startingProgressStep
        val instigator = MoveCPBeneficiaryInitiator(
                acceptorParty,
                IndiaCPDealPayload(contractStateAndRef, notary),
                myKey,
                progressTracker.getChildProgressTracker(startingProgressStep)!!)
        val stx = subFlow(instigator)

        progressTracker.currentStep = COPYING_TO_PARTICIPANTS
        val parties = contractStateAndRef.state.data.parties
        val initiator = serviceHub.myInfo.legalIdentity
        if (parties.isNotEmpty()) {
            // Copy the transaction to other participant nodes
            parties.filter{!it.equals(initiator)}.filter { !it.equals(acceptorParty) }.forEach { send(it, stx) }
        }

        return stx
    }

}

/**
 * NSDL Side of the Flow for Initiating the New ISIN Creation Flow
 */
open class MoveCPBeneficiaryInitiator(override val otherParty: Party,
                                 override val payload: IndiaCPDealPayload,
                                 override val myKeyPair: KeyPair,
                                 override val progressTracker: ProgressTracker = TwoPartyDealFlow.Primary.tracker()) : TwoPartyDealFlow.Primary() {

    override val notaryNode: NodeInfo get() =
    serviceHub.networkMapCache.notaryNodes.filter { it.notaryIdentity == payload.notary }.single()

}

/**
 * Issuer Side of the Flow for Accepting the Newly Generated ISIN.
 * We could plug in some verification logic to ensure that the generated ISIN is authentic.
 * At the moment it assumes that the signature of the Depository on the transaction is sufficient as a proof of authenticity
 */
open class MoveCPBeneficiaryAcceptor(override val otherParty: Party,
                                override val progressTracker: ProgressTracker = TwoPartyDealFlow.Secondary.tracker()) : TwoPartyDealFlow.Secondary<IndiaCPDealPayload>() {

    override fun validateHandshake(handshake: TwoPartyDealFlow.Handshake<IndiaCPDealPayload>): TwoPartyDealFlow.Handshake<IndiaCPDealPayload> {
        val cpStateAndRef = handshake.payload.contractStateAndRef
        if (cpStateAndRef.state.data is IndiaCommercialPaper.State)
            return handshake
        else
            throw IndiaCPException("Unexpected '${cpStateAndRef.state.data.javaClass.simpleName}' Payload Received in ${this.javaClass.simpleName} Flow Logic. Expected: ${IndiaCommercialPaperProgram.State::class.java.name}", Error.SourceEnum.DL_R3CORDA)
    }

    override fun assembleSharedTX(handshake: TwoPartyDealFlow.Handshake<IndiaCPDealPayload>): Pair<TransactionBuilder, List<CompositeKey>> {
        val cpStateAndRef = handshake.payload.contractStateAndRef as StateAndRef<IndiaCommercialPaper.State>

        val tx = IndiaCommercialPaper().generateMoveBeneficiaryOwnership(cpStateAndRef, handshake.payload.notary)

        // And add a request for timestamping
        tx!!.setTime(serviceHub.clock.instant(), 30.seconds)

        return Pair(tx, arrayListOf(cpStateAndRef.state.data.investor.owningKey))
    }
}

data class IndiaCPDealPayload(val contractStateAndRef: StateAndRef<IndiaCommercialPaper.State>, val notary: Party){}
package com.barclays.indiacp.cordapp.protocol.agreements

import co.paralleluniverse.fibers.Suspendable
import com.barclays.indiacp.cordapp.contract.IndiaCommercialPaper
import com.barclays.indiacp.cordapp.contract.IndiaCommercialPaperProgram
import com.barclays.indiacp.model.IndiaCPDocumentDetails
import net.corda.core.contracts.LinearState
import net.corda.core.contracts.StateAndRef
import net.corda.core.contracts.TransactionState
import net.corda.core.crypto.Party
import net.corda.core.flows.FlowLogic
import net.corda.core.node.NodeInfo
import net.corda.core.node.PluginServiceHub
import net.corda.core.serialization.SingletonSerializeAsToken
import net.corda.core.transactions.SignedTransaction
import net.corda.core.utilities.ProgressTracker
import net.corda.flows.TwoPartyDealFlow
import java.security.KeyPair
import kotlin.reflect.KClass
import kotlin.reflect.primaryConstructor

/**
 * NOT BEING USED
 * Created by ritukedia
 */
abstract class IndiaCPTwoPartyAgreementFlow() : FlowLogic<SignedTransaction>() {

    val notary = serviceHub.networkMapCache.notaryNodes.first().notaryIdentity

    val myKey = serviceHub.legalIdentityKey

    abstract val contractStateAndRef: StateAndRef<LinearState>

    abstract val tracker: ProgressTracker

    abstract val acceptorParty: Party

    abstract val startingProgressStep: ProgressTracker.Step

    override val progressTracker = tracker
}

data class IndiaCPPayload(val contractStateAndRef: StateAndRef<LinearState>, val docType: IndiaCPDocumentDetails.DocTypeEnum, val notary: Party) {

    fun  getCPProgramStateAndRef(): StateAndRef<IndiaCommercialPaperProgram.State> {
        val contractStateAndRef = contractStateAndRef
        val cpProgramContractState = contractStateAndRef.state.data as IndiaCommercialPaperProgram.State
        val cpProgramTransactionState = TransactionState<IndiaCommercialPaperProgram.State>(cpProgramContractState, contractStateAndRef.state.notary)
        val cpProgramStateAndRef : StateAndRef<IndiaCommercialPaperProgram.State> = StateAndRef(state = cpProgramTransactionState,
                ref = contractStateAndRef.ref)

        return cpProgramStateAndRef
    }

    fun  getCPStateAndRef(): StateAndRef<IndiaCommercialPaper.State> {
        val contractStateAndRef = contractStateAndRef
        val cpContractState = contractStateAndRef.state.data as IndiaCommercialPaper.State
        val cpTransactionState = TransactionState<IndiaCommercialPaper.State>(cpContractState, contractStateAndRef.state.notary)
        val cpStateAndRef : StateAndRef<IndiaCommercialPaper.State> = StateAndRef(state = cpTransactionState, ref = contractStateAndRef.ref)

        return cpStateAndRef
    }
}

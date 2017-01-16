package com.barclays.indiacp.cordapp.protocol.agreements

import com.barclays.indiacp.cordapp.contract.IndiaCommercialPaper
import com.barclays.indiacp.cordapp.contract.IndiaCommercialPaperProgram
import com.barclays.indiacp.model.IndiaCPDocumentDetails
import net.corda.core.contracts.LinearState
import net.corda.core.contracts.StateAndRef
import net.corda.core.contracts.TransactionState
import net.corda.core.crypto.Party

/**
 * Created by ritukedia
 */
data class IndiaCPDocumentPayload(val contractStateAndRef: StateAndRef<LinearState>, val docType: IndiaCPDocumentDetails.DocTypeEnum, val notary: Party) {

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


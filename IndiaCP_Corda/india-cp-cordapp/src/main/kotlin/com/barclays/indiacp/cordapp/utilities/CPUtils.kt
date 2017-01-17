package com.barclays.indiacp.cordapp.utilities

import com.barclays.indiacp.cordapp.contract.IndiaCommercialPaper
import com.barclays.indiacp.cordapp.contract.IndiaCommercialPaperProgram
import com.barclays.indiacp.cordapp.search.IndiaCPHistorySearch
import com.barclays.indiacp.model.*
import net.corda.contracts.asset.DUMMY_CASH_ISSUER_KEY
import net.corda.core.contracts.*
import net.corda.core.crypto.Party
import net.corda.core.crypto.SecureHash
import net.corda.core.crypto.composite
import net.corda.core.node.ServiceHub
import net.corda.core.node.services.linearHeadsOfType
import net.corda.core.serialization.OpaqueBytes
import net.corda.core.transactions.WireTransaction

object CPUtils {

    inline fun <reified T : ContractState> getContractStateAndRef(serviceHub: ServiceHub) : StateAndRef<T>? {
        val states = serviceHub.vaultService.currentVault.statesOfType<T>()
        return if (states.isEmpty()) null else states[0]
    }

    inline fun<reified T: LinearState> getTransactionHistory(services: ServiceHub, referenceSelector: T.() -> Boolean ): List<T> {
        val states = services.vaultService.linearHeadsOfType<T>().filterValues{ referenceSelector(it.state.data)}
        if (states == null || states.values == null || states.values.isEmpty()) {
            throw IndiaCPException("History Fetch Error", Error.SourceEnum.DL_R3CORDA)
        }
        val stx = services.storageService.validatedTransactions.getTransaction(states.values.first()!!.ref.txhash)

        val search = IndiaCPHistorySearch(services.storageService.validatedTransactions, listOf(stx!!.tx))
        search.query = IndiaCPHistorySearch.QueryByInputStateType(followInputsOfType = T::class.java)
        val txHistory : List<WireTransaction> = search.call()

        return search.filterLinearStatesOfType<T>(txHistory)

    }

    inline fun<reified T: LinearState, reified C: CommandData> getDocumentTransactionHistory(services: ServiceHub, referenceSelector: T.() -> Boolean ): List<T> {
        val states = services.vaultService.linearHeadsOfType<T>().filterValues{ referenceSelector(it.state.data)}
        if (states == null || states.values == null || states.values.isEmpty()) {
            throw IndiaCPException("History Fetch Error", Error.SourceEnum.DL_R3CORDA)
        }
        val stx = services.storageService.validatedTransactions.getTransaction(states.values.first()!!.ref.txhash)
        val search = IndiaCPHistorySearch(services.storageService.validatedTransactions, listOf(stx!!.tx))
        search.query = IndiaCPHistorySearch.QueryByCommand(withCommandOfType = C::class.java, followInputsOfType = T::class.java)
        val txHistory : List<WireTransaction> = search.call()

        return search.filterLinearStatesOfType<T>(txHistory)

    }

    fun getCPProgramStateRefNonNull(services: ServiceHub, cpProgramId: String): StateAndRef<IndiaCommercialPaperProgram.State> {
        val states = services.vaultService.linearHeadsOfType<IndiaCommercialPaperProgram.State>().filterValues { it.state.data.programId == cpProgramId }
        if (states == null || states.isEmpty()) {
            throw IndiaCPException(CPProgramError.DOES_NOT_EXIST_ERROR, Error.SourceEnum.DL_R3CORDA)
        }

        val cpProgramStateAndRef : StateAndRef<IndiaCommercialPaperProgram.State> = states.values.first()

        return cpProgramStateAndRef
    }

    fun getCPStateRefNonNull(services: ServiceHub, cpTradeId: String): StateAndRef<IndiaCommercialPaper.State> {
        val states = services.vaultService.linearHeadsOfType<IndiaCommercialPaper.State>().filterValues { it.state.data.cpTradeID == cpTradeId }
        if (states == null || states.isEmpty()) {
            throw IndiaCPException(CPIssueError.DOES_NOT_EXIST_ERROR, Error.SourceEnum.DL_R3CORDA)
        }

        val cpStateAndRef : StateAndRef<IndiaCommercialPaper.State> = states.values.first()

        return cpStateAndRef
    }

    fun getPartyAndRef(party: Party, reference: OpaqueBytes) : PartyAndReference {
        return PartyAndReference(party, reference)
    }

    fun getPartyByName(serviceHub: ServiceHub, partyName: String) : Party {
        if (serviceHub.networkMapCache.getNodeByLegalName(partyName) == null) {
            throw IndiaCPException ("Corda Node ${partyName} is Not Available", Error.SourceEnum.DL_R3CORDA)
        }
        return serviceHub.networkMapCache.getNodeByLegalName(partyName)!!.legalIdentity
    }

    fun getReference(cpRefId: String): OpaqueBytes {
        return OpaqueBytes.of(*cpRefId.toByteArray())
    }

    val DUMMY_CASH_ISSUER by lazy { Party("Snake Oil Issuer", DUMMY_CASH_ISSUER_KEY.public.composite).ref(1) }

    fun getCashIssuerForThisNode(serviceHub: ServiceHub) : PartyAndReference {
        return serviceHub.myInfo.legalIdentity.ref(1)
    }

    fun getCashIssuerForThisNode(party: Party) : PartyAndReference {
        return party.ref(1)
    }

    fun  getDocHashAndStatus(docId: String?): Pair<SecureHash, IndiaCPDocumentDetails.DocStatusEnum> {
        if (docId == null || docId.isEmpty()) {
            throw IndiaCPException(CPProgramError.DOC_UPLOAD_ERROR, Error.SourceEnum.DL_R3CORDA)
        }
        val docHashAndStatus = docId?.split(":")
        val docHash = SecureHash.parse(docHashAndStatus[0])
        val docStatus = IndiaCPDocumentDetails.DocStatusEnum.fromValue(docHashAndStatus[1])
        return Pair(docHash, docStatus)
    }

    fun  getAllCP(services: ServiceHub, cpProgramId: String): List<StateAndRef<IndiaCommercialPaper.State>> {
        val states = services.vaultService.linearHeadsOfType<IndiaCommercialPaper.State>()
        val indiacps = states.filter{it.value.state.data.cpProgramID.equals(cpProgramId)}
        return indiacps.values.toList()

    }
}
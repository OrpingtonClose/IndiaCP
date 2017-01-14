package com.barclays.indiacp.cordapp.utilities

import com.barclays.indiacp.cordapp.contract.CreditRating
import com.barclays.indiacp.cordapp.contract.IndiaCommercialPaper
import com.barclays.indiacp.cordapp.contract.IndiaCommercialPaperProgram
import com.barclays.indiacp.cordapp.search.IndiaCPHistorySearch
import com.barclays.indiacp.model.CPProgramError
import com.barclays.indiacp.model.Error
import com.barclays.indiacp.model.IndiaCPDocumentDetails
import com.barclays.indiacp.model.IndiaCPException
import net.corda.contracts.asset.DUMMY_CASH_ISSUER_KEY
import net.corda.core.contracts.*
import net.corda.core.crypto.Party
import net.corda.core.crypto.composite
import net.corda.core.node.NodeInfo
import net.corda.core.node.ServiceHub
import net.corda.core.node.services.dealsWith
import net.corda.core.node.services.linearHeadsOfType
import net.corda.core.serialization.OpaqueBytes
import net.corda.core.transactions.WireTransaction
import java.io.PrintWriter
import java.io.StringWriter
import javax.ws.rs.core.Response
import kotlin.reflect.KClass

enum class Role {
    BUYER,
    SELLER
}

val DEFAULT_BASE_DIRECTORY = "./build/indiacpdemo"

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


    fun getContractState(serviceHub: ServiceHub, cpRefId: String) : StateAndRef<OwnableState> {
        val states = serviceHub.vaultService.currentVault.statesOfType<IndiaCommercialPaper.State>()
        for (stateAndRef: StateAndRef<IndiaCommercialPaper.State> in states) {
            val ref = getReference(cpRefId)
            //TODO
//            if (stateAndRef.state.data.issuance.reference.equals(ref)) {
//                return stateAndRef
//            }
        }
        throw Exception("ECPTradeAndSettlementProtocol: Commercial Paper referenced by $cpRefId not found")
    }

    fun getPartyAndRef(party: Party, reference: OpaqueBytes) : PartyAndReference {
        return PartyAndReference(party, reference)
    }

    fun getPartyByName(serviceHub: ServiceHub, partyName: String) : Party {
        return checkNotNull(serviceHub.networkMapCache.getNodeByLegalName(partyName)).legalIdentity
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
}
package com.barclays.indiacp.cordapp.utilities

import com.barclays.indiacp.cordapp.contract.IndiaCommercialPaper
import net.corda.core.contracts.OwnableState
import net.corda.core.contracts.PartyAndReference
import net.corda.core.contracts.StateAndRef
import net.corda.core.messaging.CordaRPCOps
import net.corda.core.node.ServiceHub
import net.corda.core.node.services.dealsWith
import net.corda.core.serialization.OpaqueBytes

enum class Role {
    BUYER,
    SELLER
}

val DEFAULT_BASE_DIRECTORY = "./build/indiacpdemo"

object CPUtils {

    fun getReferencedCommercialPaper(serviceHub: ServiceHub, cpRefId: String) : StateAndRef<OwnableState> {
        val states = serviceHub.vaultService.currentVault.statesOfType<IndiaCommercialPaper.State>()
        for (stateAndRef: StateAndRef<IndiaCommercialPaper.State> in states) {
            val ref = getReference(cpRefId)
            if (stateAndRef.state.data.issuance.reference.equals(ref)) {
                return stateAndRef
            }
        }
        throw Exception("ECPTradeAndSettlementProtocol: Commercial Paper referenced by $cpRefId not found")
    }

    fun getReference(cpRefId: String): OpaqueBytes {
        return OpaqueBytes.of(*cpRefId.toByteArray())
    }
}
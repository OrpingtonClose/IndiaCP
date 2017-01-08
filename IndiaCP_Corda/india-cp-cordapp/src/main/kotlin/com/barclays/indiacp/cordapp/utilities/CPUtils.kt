package com.barclays.indiacp.cordapp.utilities

import com.barclays.indiacp.cordapp.contract.IndiaCommercialPaper
import com.barclays.indiacp.model.Error
import com.barclays.indiacp.model.IndiaCPException
import net.corda.core.contracts.OwnableState
import net.corda.core.contracts.PartyAndReference
import net.corda.core.contracts.StateAndRef
import net.corda.core.crypto.Party
import net.corda.core.node.NodeInfo
import net.corda.core.node.ServiceHub
import net.corda.core.node.services.dealsWith
import net.corda.core.serialization.OpaqueBytes
import java.io.PrintWriter
import java.io.StringWriter
import javax.ws.rs.core.Response

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
            //TODO
//            if (stateAndRef.state.data.issuance.reference.equals(ref)) {
//                return stateAndRef
//            }
        }
        throw Exception("ECPTradeAndSettlementProtocol: Commercial Paper referenced by $cpRefId not found")
    }

    fun errorHttpResponse(ex: Throwable, errorCode: Any = "Unknown", errorMessage: String? = null, errorDetails: String? = null): Response {
        if (ex is IndiaCPException)
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getError()).build()
        else {
            val error = Error()
            error.source(Error.SourceEnum.DL_R3CORDA)
            error.code(errorCode.toString())
            error.message(errorMessage ?: ex.message)
            error.details(errorDetails ?: getCustomStackTrace(ex))
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error).build()
        }
    }

    fun getStackTrace(aThrowable: Throwable): String {
        val result = StringWriter()
        val printWriter = PrintWriter(result)
        aThrowable.printStackTrace(printWriter)
        return result.toString()
    }

    /**
     * Defines a custom format for the stack trace as String.
     */
    fun getCustomStackTrace(aThrowable: Throwable): String {
        //add the class name and any message passed to constructor
        val result = StringBuilder("BOO-BOO: ")
        result.append(aThrowable.toString())
        val NEW_LINE = System.getProperty("line.separator")
        result.append(NEW_LINE)

        //add each element of the stack trace
        val element: StackTraceElement? = null
        for(element in aThrowable.stackTrace) {
            result.append(element)
            result.append(NEW_LINE)
        }
        return result.toString()
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

}
package com.barclays.indiacp.cordapp.api

import com.barclays.indiacp.cordapp.contract.IndiaCommercialPaper
import com.barclays.indiacp.cordapp.protocol.issuer.DealEntryFlow
import com.barclays.indiacp.cordapp.protocol.issuer.IssueCPFlow
import com.barclays.indiacp.cordapp.protocol.issuer.AddSettlementDetailsFlow
import com.barclays.indiacp.cordapp.utilities.CPUtils
import net.corda.contracts.testing.fillWithSomeTestCash
import net.corda.core.contracts.*
import net.corda.node.services.messaging.CordaRPCOps
//import net.corda.core.messaging.CordaRPCOps
//import net.corda.core.messaging.startFlow
import net.corda.core.serialization.OpaqueBytes
import net.corda.core.utilities.Emoji
import net.corda.core.utilities.loggerFor
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response
import net.corda.flows.CashCommand
import net.corda.flows.CashFlow
import net.corda.core.contracts.DOLLARS
import net.corda.core.contracts.Amount
import net.corda.core.node.ServiceHub
import net.corda.core.node.services.linearHeadsOfType
import net.corda.flows.CashFlowResult
import net.corda.node.services.messaging.startFlow
import java.time.Instant


@Path("indiacp")
class IndiaCPApi(val services: ServiceHub) {

    val notaryName = "Controller" //todo: remove hardcoding

    data class CPReferenceAndAcceptablePrice(val cpRefId: String, val acceptablePrice: Int)

    data class CPJSONObject(val issuer: String,
                            val beneficiary: String,
                            val ipa: String,
                            val depository: String,
                            val cpProgramID: String,
                            val cpTradeID: String,
                            val tradeDate: String,
                            val valueDate: String,
                            val faceValue: Double,
                            val maturityDays: Int,
                            val isin: String
                            )

    data class SettlementDetailsJSONObject(
                            val partyType : String,
                            val paymentAccountDetailsJSONObject: PaymentAccountDetailsJSONObject,
                            val depositoryAccountDetailsJSONObject: DepositoryAccountDetailsJSONObject
    )

    data class PaymentAccountDetailsJSONObject (
            val creditorName: String,
            val bankAccountDetails: String,
            val bankName: String,
            val rtgsCode: String
    )

    data class DepositoryAccountDetailsJSONObject (
            val dpName: String,
            val clientId: String,
            val dpID: String
    )

    data class Cash(val amount: Int)

    private companion object {
        val logger = loggerFor<IndiaCPApi>()
    }
    //private val logger = loggerFor<IndiaCPApi>()

    @POST
    @Path("issueCP")
    @Consumes(MediaType.APPLICATION_JSON)
    fun issueCP(newCP: CPJSONObject): Response {
        try {
            val stx = services.invokeFlowAsync(IssueCPFlow::class.java, newCP).resultFuture.get()
            logger.info("CP Issued\n\nFinal transaction is:\n\n${Emoji.renderIfSupported(stx.tx)}")
            return Response.status(Response.Status.OK).build()
        } catch (ex: Throwable) {
            logger.info("Exception when creating deal: ${ex.toString()}")
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.toString()).build()
        }
    }

    @POST
    @Path("addSettlementDetails/{cpIssueId}")
    @Consumes(MediaType.APPLICATION_JSON)
    fun addSettlementDetails(@PathParam("cpIssueId") cpIssueId: String, settlementDetails: SettlementDetailsJSONObject): Response {
        try {
            val stx = services.invokeFlowAsync(AddSettlementDetailsFlow::class.java, cpIssueId, settlementDetails).resultFuture.get()
//            val stx = rpc.startFlow(::AddSettlementDetailsFlow, cpTradeID, settlementDetails).returnValue.toBlocking().first()
            logger.info("Issuer Settlement Details added to CP $cpIssueId\n\nModified transaction is:\n\n${Emoji.renderIfSupported(stx.tx)}")
            return Response.status(Response.Status.OK).build()
        } catch (ex: Throwable) {
            logger.info("Exception when Issuer Settlement Details: ${ex.toString()}")
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.toString()).build()
        }
    }

    @POST
    @Path("addDealConfirmationDocs/{cpIssueId}/{docHashId}/{docStatus}")
    fun addDealConfirmationDocs(@PathParam("cpIssueId") cpIssueId: String,
                              @PathParam("docHashId") docHashId: String,
                              @PathParam("docStatus") docStatus: String): Response {
        try {
            //TODO: Add code here
            return Response.status(Response.Status.OK).build()
        } catch (ex: Throwable) {
            logger.info("Exception when creating deal: ${ex.toString()}")
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.toString()).build()
        }
    }

    @POST
    @Path("issueCash")
    @Consumes(MediaType.APPLICATION_JSON)
    fun issueCash(amount: Cash): Response {
        val notary = services.networkMapCache.notaryNodes.single { it.legalIdentity.name == notaryName }.notaryIdentity
        services.fillWithSomeTestCash(amount.amount.DOLLARS,
                outputNotary = notary,
                ownedBy = services.myInfo.legalIdentity.owningKey)
        return Response.status(Response.Status.CREATED).build()

//        val notary = services.n
//                networkMapUpdates().first.first { it.legalIdentity.name == notaryName }
//        val cashOwner = rpc.nodeIdentity()
//
//        rpc.startFlow(::CashFlow, CashCommand.IssueCash(
//                                                        amount = amount.amount.DOLLARS,
//                                                        issueRef = OpaqueBytes.of(1),
//                                                        recipient = cashOwner.legalIdentity,
//                                                        notary = notary.notaryIdentity)
//        ).returnValue.toBlocking().first() is CashFlowResult.Success
//
//        return Response.status(Response.Status.CREATED).build()
    }

    @POST
    @Path("getTransactionHistory/{cpIssueId}")
    fun getTransactionHistory(@PathParam("cpIssueId") cpIssueId: String): Response {
        try {
            //TODO: Add code here
            return Response.status(Response.Status.OK).build()
        } catch (ex: Throwable) {
            logger.info("Exception when creating deal: ${ex.toString()}")
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.toString()).build()
        }
    }

    @POST
    @Path("getDocumentHistory/{cpIssueId}/{docType}/{docSubType}")
    fun getDocumentHistory(@PathParam("cpIssueId") cpIssueId: String,
                           @PathParam("docType") docType: String,
                           @PathParam("docSubType") docSubType: String): Response {
        try {
            //TODO: Add code here
            return Response.status(Response.Status.OK).build()
        } catch (ex: Throwable) {
            logger.info("Exception when creating deal: ${ex.toString()}")
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.toString()).build()
        }
    }


    @GET
    @Path("fetchAllCP")
    @Produces(MediaType.APPLICATION_JSON)
    fun fetchAllCP(): Array<IndiaCommercialPaper.State>?  {
        try {
            return getAllCP()
        } catch (ex: Throwable) {
            logger.info("Exception when fetching ecp: ${ex.toString()}")
            return null
        }
    }

    private fun getAllCP(): Array<IndiaCommercialPaper.State>?  {
        val states = services.vaultService.linearHeadsOfType<IndiaCommercialPaper.State>()
        val indiacps = states.values.map { it.state.data }.toTypedArray()
        return indiacps
//        rpc.vaultAndUpdates().first.filterStatesOfType<IndiaCommercialPaper.State>()
//        val indiacps = states.map { it.state.data }.toTypedArray()
//        return indiacps
    }

    @GET
    @Path("fetchCP/{ref}")
    @Produces(MediaType.APPLICATION_JSON)
    fun fetchCPWithRef(@PathParam("ref") ref: String): IndiaCommercialPaper.State? {
        return getCP(ref)
    }

    private fun getCP(ref: String): IndiaCommercialPaper.State? {
        val states = services.vaultService.linearHeadsOfType<IndiaCommercialPaper.State>().filterValues { it.state.data.ref == ref }
        return if (states.isEmpty()) null else {
            val deals = states.values.map { it.state.data }
            return if (deals.isEmpty()) null else deals[0]
        }
//        val states = rpc.vaultAndUpdates().first.filterStatesOfType<IndiaCommercialPaper.State>().filter { it.state.data.ref == ref }
//        return if (states.isEmpty()) null else {
//            val deals = states.map { it.state.data }
//            return if (deals.isEmpty()) null else deals[0]
//        }
    }

    @POST
    @Path("enterDeal/{investor}")
    @Consumes(MediaType.APPLICATION_JSON)
    fun enterTrade(cp : CPReferenceAndAcceptablePrice, @PathParam("investor") investorName: String): Response? {
        try {
            if (investorName != null) {
                val investor = services.identityService.partyFromName(investorName)
                val stx = services.invokeFlowAsync(DealEntryFlow::class.java, cp.cpRefId, investor).resultFuture.get()
//                val stx = rpc.startFlow(::DealEntryFlow, cp.cpRefId, investorName).returnValue.toBlocking().first()
                logger.info("CP Deal Finalized\n\nFinal transaction is:\n\n${Emoji.renderIfSupported(stx.tx)}")
                return Response.status(Response.Status.OK).build()
            } else {
                return Response.status(Response.Status.BAD_REQUEST).entity("Invalid Investor reference").build()
            }
        } catch (ex: Throwable) {
            logger.info("Exception when fetching ecp: ${ex.toString()}")
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.toString()).build()
        }
    }
}

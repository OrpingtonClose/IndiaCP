package com.barclays.indiacp.cordapp.api

import com.barclays.indiacp.cordapp.contract.IndiaCommercialPaper
import com.barclays.indiacp.cordapp.protocol.agreements.AddCPDocFlow
import com.barclays.indiacp.cordapp.protocol.issuer.IssueCPFlow
import com.barclays.indiacp.cordapp.utilities.CPUtils
import com.barclays.indiacp.cordapp.utilities.ErrorUtils
import com.barclays.indiacp.cordapp.utilities.ModelUtils
import com.barclays.indiacp.model.*
import net.corda.contracts.testing.fillWithSomeTestCash
import net.corda.core.contracts.DOLLARS
import net.corda.core.contracts.StateAndRef
import net.corda.core.crypto.Party
import net.corda.core.node.ServiceHub
import net.corda.core.node.services.linearHeadsOfType
import net.corda.core.utilities.Emoji
import net.corda.core.utilities.loggerFor
import java.time.Instant
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response


/*
 * This is the REST Client API for India CP Trade Operations
 *
 * Created by ritukedia on 07/01/17.
 */
@Path("indiacpissue")
class IndiaCPApi(val services: ServiceHub){
    data class CPReferenceAndAcceptablePrice(val cpRefId: String, val acceptablePrice: Int)
    data class Cash(val amount: Int)

    private companion object {
        val logger = loggerFor<IndiaCPApi>()
    }

    @POST
    @Path("issueCP")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    fun issueCP(indiaCPModel: IndiaCPIssue): Response {
        try
        {
            val contractState = ModelUtils.indiaCPStateFromModel(indiaCPModel, services)
            val stx = services.invokeFlowAsync(IssueCPFlow::class.java, contractState).resultFuture.get()
            logger.info("CP Issued under CP Program ${indiaCPModel.cpProgramId} \n\nFinal transaction is:\n\n${Emoji.renderIfSupported(stx.tx)}")

            val createdContractState = getCP(indiaCPModel.cpTradeId) ?: throw IndiaCPException(CPIssueError.CREATION_ERROR, Error.SourceEnum.DL_R3CORDA, "Could not fetch the newly created CP from the DL");
            return Response.status(Response.Status.OK).entity(ModelUtils.indiaCPModelFromState(createdContractState!!)).build()

        } catch (ex: Throwable) {
            logger.info("${CPIssueError.CREATION_ERROR}: ${ex.toString()}")
            return ErrorUtils.errorHttpResponse(ex, errorCode = CPIssueError.CREATION_ERROR)
        }
    }

    @POST
    @Path("issueCash")
    @Consumes(MediaType.APPLICATION_JSON)
    fun issueCash(amount: Cash): Response {
        val notary = services.networkMapCache.notaryNodes.first().notaryIdentity
        services.fillWithSomeTestCash(amount.amount.DOLLARS,
                outputNotary = notary,
                ownedBy = services.myInfo.legalIdentity.owningKey)
        return Response.status(Response.Status.CREATED).build()
    }

    @GET
    @Path("getTransactionHistory/{cpTradeId}")
    @Produces(MediaType.APPLICATION_JSON)
    fun getTransactionHistory(@PathParam("cpTradeId") cpTradeId: String): Response {
        try {
            val history = CPUtils.getTransactionHistory<IndiaCommercialPaper.State>(services, { cpTradeId == cpTradeId })
            return Response.status(Response.Status.OK).entity(history.map { ModelUtils.indiaCPModelFromState(it) }).build()
        } catch (ex: Throwable) {
            logger.info("${CPIssueError.HISTORY_SEARCH_ERROR}: ${ex.toString()}")
            return ErrorUtils.errorHttpResponse(ex, errorCode = CPIssueError.HISTORY_SEARCH_ERROR)
        }
    }

    @GET
    @Path("getDocumentHistory/{cpTradeId}/{docType}/{docSubType}")
    @Produces(MediaType.APPLICATION_JSON)
    fun getDocumentHistory(@PathParam("cpTradeId") cpTradeId: String,
                           @PathParam("docType") docType: String,
                           @PathParam("docSubType") docSubType: String): Response {
        var history: List<IndiaCPDocumentDetails> = emptyList()
        try {
            when (docType) {
                IndiaCPDocumentDetails.DocTypeEnum.DEAL_CONFIRMATION_DOC.name -> {
                    val cpStatesForDocTypeTransactions = CPUtils.getDocumentTransactionHistory<IndiaCommercialPaper.State, IndiaCommercialPaper.Commands.AddDealConfirmationDoc>(services, { cpTradeId == cpTradeID })
                    history = ModelUtils.getDocumentDetailsForCP(cpTradeId, cpStatesForDocTypeTransactions, IndiaCPDocumentDetails.DocTypeEnum.DEPOSITORY_DOCS)
                }
                else -> {
                    return ErrorUtils.errorHttpResponse(IndiaCPException("Unknown Document Type History Requested from India Commercial Paper Smart Contract", Error.SourceEnum.DL_R3CORDA),
                            errorCode = CPProgramError.DOC_UPLOAD_ERROR)
                }
            }
            return Response.status(Response.Status.OK).entity(history).build()
        } catch (ex: Throwable) {
            logger.info("${CPIssueError.HISTORY_SEARCH_ERROR}: ${ex.toString()}")
            return ErrorUtils.errorHttpResponse(ex, errorCode = CPIssueError.HISTORY_SEARCH_ERROR)
        }
    }

    @GET
    @Path("fetchAllCP")
    @Produces(MediaType.APPLICATION_JSON)
    fun fetchAllCP(): Response {
        try {
            val cpArray = getAllCP()
            return Response.status(Response.Status.OK).entity(cpArray?.map { ModelUtils.indiaCPModelFromState(it) }).build()
        } catch (ex: Throwable) {
            logger.info("${CPIssueError.FETCH_ERROR}: ${ex.toString()}")
            return ErrorUtils.errorHttpResponse(ex, errorCode = CPIssueError.FETCH_ERROR)
        }
    }

    private fun getAllCP(): Array<IndiaCommercialPaper.State>?  {
        val states = services.vaultService.linearHeadsOfType<IndiaCommercialPaper.State>()
        val indiacps = states.values.map { it.state.data }.toTypedArray()
        return indiacps
    }

    @GET
    @Path("fetchCP/{cpIssueId}")
    @Produces(MediaType.APPLICATION_JSON)
    fun fetchCP(@PathParam("cpIssueId") cpIssueId: String): Response {
        try {
            val cp = getCP(cpIssueId)

            return Response.status(Response.Status.OK).entity(if (cp == null) "" else ModelUtils.indiaCPModelFromState(cp)).build()
        } catch (ex: Throwable) {
            logger.info("Exception when fetching ecp: ${ex.toString()}")
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.toString()).build()
        }
    }

    private fun getCP(ref: String): IndiaCommercialPaper.State? {
        val states = services.vaultService.linearHeadsOfType<IndiaCommercialPaper.State>().filterValues { it.state.data.ref == ref }
        return if (states.isEmpty()) null else {
            val deals = states.values.map { it.state.data }
            return if (deals.isEmpty()) null else deals[0]
        }
    }

    @POST
    @Path("addDocs/{cpIssueId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    fun addDocs(@PathParam("cpIssueId") cpIssueId: String,
                docDetails:IndiaCPDocumentDetails): Response {
        try
        {
            val cpStateAndRef : StateAndRef<IndiaCommercialPaper.State> = CPUtils.getCPStateRefNonNull(services, cpIssueId)

            when (docDetails.docType) {
                IndiaCPDocumentDetails.DocTypeEnum.DEAL_CONFIRMATION_DOC -> {
                    val newStateRef = cpStateAndRef.copy(state = cpStateAndRef.state.copy(
                            data = cpStateAndRef.state.data.copy(dealConfirmationDocId = docDetails.docHash + ":" + docDetails.docStatus ?:  IndiaCPDocumentDetails.DocStatusEnum.UNKNOWN.name,
                                    modifiedBy = docDetails.modifiedBy,
                                    lastModifiedDate = docDetails.lastModifiedDate?.toInstant() ?: Instant.now()
                            )
                    )
                    )
                    val stx = services.invokeFlowAsync(AddCPDocFlow::class.java, newStateRef, docDetails.docType, getOtherPartyForDealConfirmation(cpStateAndRef)).resultFuture.get()
                    logger.info("ISIN Request Document Uploaded & Stamped on DL \n\nFinal transaction is:\n\n${Emoji.renderIfSupported(stx.tx)}")
                }
                else -> {
                    return ErrorUtils.errorHttpResponse(IndiaCPException("Unknown Document Type Uploaded to India Commercial Paper Smart Contract", Error.SourceEnum.DL_R3CORDA),
                            errorCode = CPIssueError.DOC_UPLOAD_ERROR)
                }
            }

            return Response.status(Response.Status.OK).entity(ModelUtils.indiaCPModelFromState(getCP(cpIssueId)!!)).build()
        } catch (ex: Throwable) {
            logger.info("${CPIssueError.DOC_UPLOAD_ERROR}: ${ex.toString()}")
            return ErrorUtils.errorHttpResponse(ex, errorCode = CPIssueError.DOC_UPLOAD_ERROR)
        }
    }

    private fun  getOtherPartyForDealConfirmation(cpStateAndRef: StateAndRef<IndiaCommercialPaper.State>): Party {
        val thisNode = services.myInfo
        if (thisNode.legalIdentity.name.equals(cpStateAndRef.state.data.issuer.name))
            return cpStateAndRef.state.data.beneficiary
        else
            return cpStateAndRef.state.data.issuer
    }


    @POST
    @Path("enterDeal/{investor}")
    @Consumes(MediaType.APPLICATION_JSON)
    fun enterTrade(cp : CPReferenceAndAcceptablePrice, @PathParam("investor") investorName: String): Response? {
        try {
            if (investorName != null) {
////                val investor = services.identityService.partyFromName(investorName)
////                val stx = services.invokeFlowAsync(DealEntryFlow::class.java, cp.cpRefId, investor).resultFuture.get()
////                val stx = rpc.startFlow(::DealEntryFlow, cp.cpRefId, investorName).returnValue.toBlocking().first()
//                logger.info("CP Deal Finalized\n\nFinal transaction is:\n\n${Emoji.renderIfSupported(stx.tx)}")
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

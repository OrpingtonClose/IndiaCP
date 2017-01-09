package com.barclays.indiacp.cordapp.api

import com.barclays.indiacp.cordapp.contract.BorrowingLimitBoardResolution
import com.barclays.indiacp.cordapp.contract.LegalEntityDocumentOwnableState
import com.barclays.indiacp.cordapp.protocol.issuer.BorrowingLimitBoardResolutionFlows
import com.barclays.indiacp.cordapp.utilities.CPUtils
import com.barclays.indiacp.cordapp.utilities.ModelUtils
import com.barclays.indiacp.model.*
import net.corda.core.node.ServiceHub
import net.corda.core.utilities.Emoji
import net.corda.core.utilities.loggerFor
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

/**
 * Created by ritukedia on 07/01/17.
 */
/*
 * This is the REST Client API for India CP Trade Operations
 * Created by ritukedia on 07/01/17.
 */
@Path("boardresolution")
class BorrowingLimitBoardResolutionApi(val services: ServiceHub){

    private companion object {
        val logger = loggerFor<BorrowingLimitBoardResolutionApi>()
    }

    @POST
    @Path("issueBoardResolution")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    fun issueBoardResolution(boardResolutionDocument: BoardResolutionBorrowingLimitDocument): Response
    {
        try {
            val boardResolutionContractState : LegalEntityDocumentOwnableState = ModelUtils.boardResolutionStateFromModel(boardResolutionDocument, services)
            val stx = services.invokeFlowAsync(BorrowingLimitBoardResolutionFlows::class.java, boardResolutionContractState, BorrowingLimitBoardResolution.Commands.Issue::class.java.simpleName).resultFuture.get()
            logger.info("Issued Credit Rating with CR Document\n\nFinal transaction is:\n\n${Emoji.renderIfSupported(stx.tx)}")

            return Response.status(Response.Status.OK).entity(getBoardResolution()).build()

        } catch (ex: Throwable) {
            logger.info("Exception when creating credit rating: ${ex.toString()}")
            return CPUtils.errorHttpResponse(ex, errorCode = CreditRatingError.CREATION_ERROR)
        }
    }

    @POST
    @Path("amendBoardResolution")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    fun amendBoardResolution(boardResolutionDocument: BoardResolutionBorrowingLimitDocument): Response
    {
        try {
            val boardResolutionContractState : LegalEntityDocumentOwnableState = ModelUtils.boardResolutionStateFromModel(boardResolutionDocument, services)
            val stx = services.invokeFlowAsync(BorrowingLimitBoardResolutionFlows::class.java, boardResolutionContractState, BorrowingLimitBoardResolution.Commands.Amend::class.java.simpleName).resultFuture.get()
            logger.info("Amended Credit Rating with CR Document\n\nFinal transaction is:\n\n${Emoji.renderIfSupported(stx.tx)}")

            return Response.status(Response.Status.OK).entity(getBoardResolution()).build()

        } catch (ex: Throwable) {
            logger.info("Exception when creating credit rating: ${ex.toString()}")
            return CPUtils.errorHttpResponse(ex, errorCode = CreditRatingError.AMENDMENT_ERROR)
        }

    }

    @GET
    @Path("cancelBoardResolution")
    @Produces(MediaType.APPLICATION_JSON)
    fun cancelBoardResolution(): Response
    {
        try {
            val stx = services.invokeFlowAsync(BorrowingLimitBoardResolutionFlows::class.java, null, BorrowingLimitBoardResolution.Commands.Cancel::class.java.simpleName).resultFuture.get()
            logger.info("Cancelled Board Resolution \n\nFinal transaction is:\n\n${Emoji.renderIfSupported(stx.tx)}")

            return Response.status(Response.Status.OK).entity("Board Resolution Document for this Legal Entity has been Cancelled").build()

        } catch (ex: Throwable) {
            logger.info("Exception when creating credit rating: ${ex.toString()}")
            return CPUtils.errorHttpResponse(ex, errorCode = CreditRatingError.CANCELLATION_ERROR)
        }
    }

    @GET
    @Path("fetchBoardResolution")
    @Produces(MediaType.APPLICATION_JSON)
    fun fetchBoardResolution(): Response
    {
        try {
            return Response.status(Response.Status.OK).entity(getBoardResolution() ?: "No Board Resolution Documents Uploaded").build()
        } catch (ex: Throwable) {
            logger.info("Exception when fetching board resolution: ${ex.toString()}")
            return CPUtils.errorHttpResponse(ex, errorCode = CreditRatingError.FETCH_ERROR)
        }
    }

    private fun getBoardResolution(): BoardResolutionBorrowingLimitDocument? {
        val states = services.vaultService.currentVault.statesOfType<BorrowingLimitBoardResolution.State>()
        return if (states.isEmpty()) null else {
            val datas = states.map { it.state.data }
            return if (datas.isEmpty()) null else ModelUtils.boardResolutionModelFromState(datas[0])
        }
    }

}

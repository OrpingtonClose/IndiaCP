package com.barclays.indiacp.cordapp.api

import com.barclays.indiacp.cordapp.contract.CreditRating
import com.barclays.indiacp.cordapp.protocol.issuer.CreditRatingFlows
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
@Path("creditrating")
class CreditRatingApi(val services: ServiceHub){

    private companion object {
        val logger = loggerFor<CreditRatingApi>()
    }

    @POST
    @Path("issueCreditRating")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    fun issueCreditRating(creditRatingDocument: LegalEntityCreditRatingDocument): Response
    {
        try {
            val stx = services.invokeFlowAsync(CreditRatingFlows::class.java, ModelUtils.creditRatingStateFromModel(creditRatingDocument, services), CreditRating.Commands.Issue::class.java.name).resultFuture.get()
            logger.info("Issued Credit Rating with CR Document\n\nFinal transaction is:\n\n${Emoji.renderIfSupported(stx.tx)}")

            return Response.status(Response.Status.OK).entity(getCreditRating()).build()

        } catch (ex: Throwable) {
            logger.info("Exception when creating credit rating: ${ex.toString()}")
            return CPUtils.errorHttpResponse(ex, errorCode = CreditRatingError.CREATION_ERROR)
        }
    }

    @POST
    @Path("amendCreditRating")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    fun amendCreditRating(creditRatingDocument: LegalEntityCreditRatingDocument): Response
    {
        try {
            val stx = services.invokeFlowAsync(CreditRatingFlows::class.java, ModelUtils.creditRatingStateFromModel(creditRatingDocument, services), CreditRating.Commands.Amend::class.java.name).resultFuture.get()
            logger.info("Amended Credit Rating with CR Document\n\nFinal transaction is:\n\n${Emoji.renderIfSupported(stx.tx)}")

            return Response.status(Response.Status.OK).entity(getCreditRating()).build()

        } catch (ex: Throwable) {
            logger.info("Exception when creating credit rating: ${ex.toString()}")
            return CPUtils.errorHttpResponse(ex, errorCode = CreditRatingError.AMENDMENT_ERROR)
        }

    }

    @GET
    @Path("cancelCreditRating")
    @Produces(MediaType.APPLICATION_JSON)
    fun cancelCreditRating(): Response
    {
        try {
            val stx = services.invokeFlowAsync(CreditRatingFlows::class.java, null, CreditRating.Commands.Cancel::class.java.name).resultFuture.get()
            logger.info("Cancelled Credit Rating with CR Document\n\nFinal transaction is:\n\n${Emoji.renderIfSupported(stx.tx)}")

            return Response.status(Response.Status.OK).entity("Credit Rating Document for this Legal Entity has been Cancelled").build()

        } catch (ex: Throwable) {
            logger.info("Exception when creating credit rating: ${ex.toString()}")
            return CPUtils.errorHttpResponse(ex, errorCode = CreditRatingError.CANCELLATION_ERROR)
        }
    }

    @GET
    @Path("fetchCreditRating")
    @Produces(MediaType.APPLICATION_JSON)
    fun fetchCreditRating(): Response
    {
        try {
            return Response.status(Response.Status.OK).entity(getCreditRating() ?: "No Credit Rating Documents Uploaded").build()
        } catch (ex: Throwable) {
            logger.info("Exception when fetching credit rating: ${ex.toString()}")
            return CPUtils.errorHttpResponse(ex, errorCode = CreditRatingError.FETCH_ERROR)
        }
    }

    private fun getCreditRating(): LegalEntityCreditRatingDocument? {
        val states = services.vaultService.currentVault.statesOfType<CreditRating.State>()
        return if (states.isEmpty()) null else {
            val datas = states.map { it.state.data }
            return if (datas.isEmpty()) null else ModelUtils.creditRatingModelFromState(datas[0])
        }
    }

}

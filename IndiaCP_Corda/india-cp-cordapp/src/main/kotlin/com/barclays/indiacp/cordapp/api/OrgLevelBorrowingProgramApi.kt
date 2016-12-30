package com.barclays.indiacp.cordapp.api


import com.barclays.indiacp.cordapp.dto.OrgLevelProgramJSON
import com.barclays.indiacp.cordapp.protocol.issuer.IssueCPProgramWithInOrgLimitFlow
import com.barclays.indiacp.cordapp.protocol.issuer.OrgLevelBorrowProgramFlow
import com.barclays.indiacp.model.CPProgram
import net.corda.core.node.ServiceHub
import net.corda.core.utilities.Emoji
import net.corda.core.utilities.loggerFor
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response


/*
 * REST APIs for Managing IndiaCommercialPaperProgram
 */
@Path("orglevelcontract")
class OrgLevelBorrowingProgramApi(val services: ServiceHub) {

    val notaryName = "Controller" //todo: remove hardcoding

    private companion object {
        val logger = loggerFor<OrgLevelBorrowingProgramApi>()
    }

    @POST
    @Path("issueOrgLevelBorrowingProgram")
    @Consumes(MediaType.APPLICATION_JSON)
    fun issueCPProgram(orgLevelProgramJSON: OrgLevelProgramJSON): Response {
        try {
            val stx = services.invokeFlowAsync(OrgLevelBorrowProgramFlow::class.java, orgLevelProgramJSON).resultFuture.get()
            logger.info("Org Borrowing Program Issued\n\nFinal transaction is:\n\n${Emoji.renderIfSupported(stx.tx)}")
            return Response.status(Response.Status.OK).build()
        } catch (ex: Throwable) {
            logger.info("Exception when creating Org Borrowing Program deal: ${ex.toString()}")
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.toString()).build()
        }
    }


    @POST
    @Path("issueCPProgramWithInOrg")
    @Consumes(MediaType.APPLICATION_JSON)
    fun issueCPProgramWithInOrg(indiaCPProgramJSON: CPProgram): Response {
        try
        {
            val stx = services.invokeFlowAsync(IssueCPProgramWithInOrgLimitFlow::class.java, indiaCPProgramJSON).resultFuture.get()
            logger.info("CP Program Issued within ORG \n\nFinal transaction is:\n\n${Emoji.renderIfSupported(stx.tx)}")
            return Response.status(Response.Status.OK).build()
        } catch (ex: Throwable) {
            logger.info("Exception when creating Org Borrowing Program deal: ${ex.toString()}")
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.toString()).build()
        }
    }

}

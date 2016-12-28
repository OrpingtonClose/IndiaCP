package com.barclays.indiacp.cordapp.api

import com.barclays.indiacp.cordapp.contract.IndiaCommercialPaperProgram
import com.barclays.indiacp.cordapp.dto.IndiaCPProgramJSON
import com.barclays.indiacp.cordapp.protocol.issuer.CPProgramFlows
import com.barclays.indiacp.cordapp.protocol.issuer.IssueCPWithinCPProgramFlow
import com.barclays.indiacp.cordapp.utilities.CP_PROGRAM_FLOW_STAGES
import net.corda.core.node.ServiceHub
import net.corda.core.node.services.linearHeadsOfType
import net.corda.core.utilities.Emoji
import net.corda.core.utilities.loggerFor
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response


/*
 * REST APIs for Managing IndiaCommercialPaperProgram
 */
@Path("indiacpprogram")
class IndiaCPProgramApi(val services: ServiceHub) {

    val notaryName = "Controller" //todo: remove hardcoding

    private companion object {
        val logger = loggerFor<IndiaCPProgramApi>()
    }

    @POST
    @Path("issueCPProgram")
    @Consumes(MediaType.APPLICATION_JSON)
    fun issueCPProgram(indiaCPProgramJSON: IndiaCPProgramJSON): Response {
        try {
            val stx = services.invokeFlowAsync(CPProgramFlows::class.java, indiaCPProgramJSON, CP_PROGRAM_FLOW_STAGES.ISSUE_CP_PROGRAM).resultFuture.get()
            logger.info("CP Program Issued\n\nFinal transaction is:\n\n${Emoji.renderIfSupported(stx.tx)}")
            return Response.status(Response.Status.OK).build()
        } catch (ex: Throwable) {
            logger.info("Exception when creating CP Program deal: ${ex.toString()}")
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.toString()).build()
        }
    }

    @POST
    @Path("addISINGenerationDocs/{cpProgramId}/{docHashId}/{docStatus}")
    fun addISINGenerationDocs(@PathParam("cpProgramId") cpProgramId: String,
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


    /*
    This method will add accept generated ISIN from NSDL and the proof of generation
    in a document from NSDL or some email.

     */
    @POST
    @Path("addISIN/{cpProgramId}/{isin}/{docHashId}/{docStatus}")
    fun addISIN(@PathParam("cpProgramId") cpProgramId: String,
                @PathParam("isin") isin: String,
                @PathParam("docHashId") docHashId: String,
                @PathParam("docStatus") docStatus: String
    ): Response
    {
        try
        {
            val indiaCPProgramJSON:IndiaCPProgramJSON = IndiaCPProgramJSON(program_id = cpProgramId, isin = isin, isin_generation_request_doc_id = docHashId, status = CP_PROGRAM_FLOW_STAGES.ADDISIN.endStatus)



            val stx = services.invokeFlowAsync(CPProgramFlows::class.java, indiaCPProgramJSON, CP_PROGRAM_FLOW_STAGES.ADDISIN).resultFuture.get()
            logger.info("CP Program Issued\n\nFinal transaction is:\n\n${Emoji.renderIfSupported(stx.tx)}")
            return Response.status(Response.Status.OK).build()
        } catch (ex: Throwable) {
            logger.info("Exception when creating deal: ${ex.toString()}")
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.toString()).build()
        }
    }

    @POST
    @Path("issueCPWithinCPProgram/{cpProgramId}")
    @Consumes(MediaType.APPLICATION_JSON)
    fun issueCPWintinCPProgram(@PathParam("cpProgramId") cpProgramId: String,
                               newCP: IndiaCPApi.CPJSONObject
                               ): Response {
        try {
            //TODO: For now we are only testing multithread updates.
            //TODO: Need to add issueCP Code at this poiont.



            val stx = services.invokeFlowAsync(IssueCPWithinCPProgramFlow::class.java, newCP).resultFuture.get()
            logger.info("Issue CP Within a CP Program\n\nFinal transaction is:\n\n${Emoji.renderIfSupported(stx.tx)}")

            return Response.status(Response.Status.OK).build()
        } catch (ex: Throwable) {
            logger.info("Exception when creating deal: ${ex.toString()}")
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.toString()).build()
        }
    }

    @POST
    @Path("addIPAVerificationDocs/{cpProgramId}/{docHashId}/{docStatus}")
    fun addIPAVerificationDocs(@PathParam("cpProgramId") cpProgramId: String,
                             @PathParam("docHashId") docHashId: String,
                             @PathParam("docStatus") docStatus: String): Response {
        try {
            val indiaCPProgramJSON:IndiaCPProgramJSON = IndiaCPProgramJSON(program_id = cpProgramId, ipa_verification_request_doc_id = docHashId, status = CP_PROGRAM_FLOW_STAGES.ADD_IPA_VERI_DOC.endStatus)



            val stx = services.invokeFlowAsync(CPProgramFlows::class.java, indiaCPProgramJSON, CP_PROGRAM_FLOW_STAGES.ADD_IPA_VERI_DOC).resultFuture.get()
            logger.info("CP Program Issued\n\nFinal transaction is:\n\n${Emoji.renderIfSupported(stx.tx)}")
            return Response.status(Response.Status.OK).build()
        } catch (ex: Throwable) {
            logger.info("Exception when creating deal: ${ex.toString()}")
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.toString()).build()
        }
    }

    @POST
    @Path("addIPACertifcateDoc/{cpProgramId}/{docHashId}/{docStatus}")
    fun addIPACertifcateDoc(@PathParam("cpProgramId") cpProgramId: String,
                               @PathParam("docHashId") docHashId: String,
                               @PathParam("docStatus") docStatus: String): Response {
        try {
            val indiaCPProgramJSON:IndiaCPProgramJSON = IndiaCPProgramJSON(program_id = cpProgramId, ipa_certificate_doc_id = docHashId, status = CP_PROGRAM_FLOW_STAGES.ADD_IPA_CERT_DOC.endStatus)



            val stx = services.invokeFlowAsync(CPProgramFlows::class.java, indiaCPProgramJSON, CP_PROGRAM_FLOW_STAGES.ADD_IPA_CERT_DOC).resultFuture.get()
            logger.info("CP Program Issued\n\nFinal transaction is:\n\n${Emoji.renderIfSupported(stx.tx)}")
            return Response.status(Response.Status.OK).build()
        } catch (ex: Throwable) {
            logger.info("Exception when creating deal: ${ex.toString()}")
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.toString()).build()
        }
    }

    @POST
    @Path("addCorpActionFormDoc/{cpProgramId}/{docHashId}/{docStatus}")
    fun addCorpActionFormDoc(@PathParam("cpProgramId") cpProgramId: String,
                            @PathParam("docHashId") docHashId: String,
                            @PathParam("docStatus") docStatus: String): Response {
        try {
            val indiaCPProgramJSON:IndiaCPProgramJSON = IndiaCPProgramJSON(program_id = cpProgramId, corporate_action_form_doc_id = docHashId, status = CP_PROGRAM_FLOW_STAGES.ADD_CORP_ACT_FORM_DOC.endStatus)



            val stx = services.invokeFlowAsync(CPProgramFlows::class.java, indiaCPProgramJSON, CP_PROGRAM_FLOW_STAGES.ADD_CORP_ACT_FORM_DOC).resultFuture.get()
            logger.info("CP Program Issued\n\nFinal transaction is:\n\n${Emoji.renderIfSupported(stx.tx)}")
            return Response.status(Response.Status.OK).build()
        } catch (ex: Throwable) {
            logger.info("Exception when creating deal: ${ex.toString()}")
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.toString()).build()
        }
    }

    @POST
    @Path("addAllotmentLetterDoc/{cpProgramId}/{docHashId}/{docStatus}")
    fun addAllotmentLetterDoc(@PathParam("cpProgramId") cpProgramId: String,
                             @PathParam("docHashId") docHashId: String,
                             @PathParam("docStatus") docStatus: String): Response {
        try {
            val indiaCPProgramJSON:IndiaCPProgramJSON = IndiaCPProgramJSON(program_id = cpProgramId, allotment_letter_doc_id = docHashId, status = CP_PROGRAM_FLOW_STAGES.ADD_ALLOT_LETTER_DOC.endStatus)



            val stx = services.invokeFlowAsync(CPProgramFlows::class.java, indiaCPProgramJSON, CP_PROGRAM_FLOW_STAGES.ADD_ALLOT_LETTER_DOC).resultFuture.get()
            logger.info("CP Program Issued\n\nFinal transaction is:\n\n${Emoji.renderIfSupported(stx.tx)}")
            return Response.status(Response.Status.OK).build()
        } catch (ex: Throwable) {
            logger.info("Exception when creating deal: ${ex.toString()}")
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.toString()).build()
        }
    }

    @POST
    @Path("closeCPProgram/{cpProgramId}")
    fun closeCPProgram(@PathParam("cpProgramId") cpProgramId: String): Response {
        try {
            //TODO: Add code here
            return Response.status(Response.Status.OK).build()
        } catch (ex: Throwable) {
            logger.info("Exception when creating deal: ${ex.toString()}")
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.toString()).build()
        }
    }

    @POST
    @Path("getTransactionHistory/{cpProgramId}")
    fun getTransactionHistory(@PathParam("cpProgramId") cpProgramId: String): Response {
        try {
            //TODO: Add code here
            return Response.status(Response.Status.OK).build()
        } catch (ex: Throwable) {
            logger.info("Exception when creating deal: ${ex.toString()}")
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.toString()).build()
        }
    }

    @POST
    @Path("getDocumentHistory/{cpProgramId}/{docType}/{docSubType}")
    fun getDocumentHistory(@PathParam("cpProgramId") cpProgramId: String,
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
    @Path("fetchAllCPProgram")
    @Produces(MediaType.APPLICATION_JSON)
    fun fetchAllCPProgram(): Array<IndiaCommercialPaperProgram.State>?  {
        try {
            return getAllCPProgram()
        } catch (ex: Throwable) {
            logger.info("Exception when fetching ecp: ${ex.toString()}")
            return null
        }
    }

    private fun getAllCPProgram(): Array<IndiaCommercialPaperProgram.State>?  {
        val states = services.vaultService.linearHeadsOfType<IndiaCommercialPaperProgram.State>()
        val indiacpprogams = states.values.map { it.state.data }.toTypedArray()
        return indiacpprogams
    }

    @GET
    @Path("fetchCPProgram/{ref}")
    @Produces(MediaType.APPLICATION_JSON)
    fun fetchCPProgramWithRef(@PathParam("ref") ref: String): IndiaCommercialPaperProgram.State? {
        return getCPProgram(ref)
    }

    private fun getCPProgram(ref: String): IndiaCommercialPaperProgram.State? {
        val states = services.vaultService.linearHeadsOfType<IndiaCommercialPaperProgram.State>().filterValues { it.state.data.programId == ref }
        return if (states.isEmpty()) null else {
            val deals = states.values.map { it.state.data }
            return if (deals.isEmpty()) null else deals[0]
        }
    }

}

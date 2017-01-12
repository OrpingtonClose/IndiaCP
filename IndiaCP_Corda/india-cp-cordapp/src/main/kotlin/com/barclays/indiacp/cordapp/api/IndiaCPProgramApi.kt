package com.barclays.indiacp.cordapp.api

//import net.corda.core.contracts.IndiaCPHistorySearch
import com.barclays.indiacp.cordapp.contract.IndiaCommercialPaperProgram
import com.barclays.indiacp.cordapp.protocol.issuer.IssueCPProgramFlow
import com.barclays.indiacp.cordapp.search.IndiaCPHistorySearch
import com.barclays.indiacp.cordapp.utilities.CPUtils
import com.barclays.indiacp.cordapp.utilities.ErrorUtils
import com.barclays.indiacp.cordapp.utilities.ModelUtils
import com.barclays.indiacp.model.*
import net.corda.core.contracts.ContractState
import net.corda.core.node.ServiceHub
import net.corda.core.node.services.linearHeadsOfType
import net.corda.core.transactions.WireTransaction
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

    private companion object {
        val logger = loggerFor<IndiaCPProgramApi>()
    }

    @POST
    @Path("issueCPProgram")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    fun issueCPProgram(indiaCPProgramModel: IndiaCPProgram): Response {
        try
        {
            val contractState = ModelUtils.indiaCPProgramStateFromModel(indiaCPProgramModel, services)
            val stx = services.invokeFlowAsync(IssueCPProgramFlow::class.java, contractState).resultFuture.get()
            logger.info("CP Program Issued within ORG \n\nFinal transaction is:\n\n${Emoji.renderIfSupported(stx.tx)}")

            val createdContractState = getCPProgram(indiaCPProgramModel.programId) ?: throw IndiaCPException(CPProgramError.CREATION_ERROR, Error.SourceEnum.DL_R3CORDA, "Could not fetch the newly created CPProgram from the DL");
            return Response.status(Response.Status.OK).entity(ModelUtils.indiaCPProgramModelFromState(createdContractState!!)).build()

        } catch (ex: Throwable) {
            logger.info("${CPProgramError.CREATION_ERROR}: ${ex.toString()}")
            return ErrorUtils.errorHttpResponse(ex, errorCode = CPProgramError.CREATION_ERROR)
        }
    }

//    @POST
//    @Path("addISINGenerationDocs/{cpProgramId}/{docHashId}/{docStatus}")
//    fun addISINGenerationDocs(@PathParam("cpProgramId") cpProgramId: String,
//                             @PathParam("docHashId") docHashId: String,
//                             @PathParam("docStatus") docStatus: String): Response {
//        try {
//            val indiaCPProgramJSON:IndiaCPProgram = IndiaCPProgram()
//
//            indiaCPProgramJSON.programId = cpProgramId
//            indiaCPProgramJSON.isin_generation_request_doc_id = docHashId , isin_generation_request_doc_status = docStatus, status = CP_PROGRAM_FLOW_STAGES.ADD_ISIN_GEN_DOC.endStatus)
//
//            val stx = services.invokeFlowAsync(CPProgramFlows::class.java, indiaCPProgramJSON, CP_PROGRAM_FLOW_STAGES.ADD_ISIN_GEN_DOC).resultFuture.get()
//            logger.info("CP Program Issued\n\nFinal transaction is:\n\n${Emoji.renderIfSupported(stx.tx)}")
//            return Response.status(Response.Status.OK).build()
//        } catch (ex: Throwable) {
//            logger.info("Exception when creating deal: ${ex.toString()}")
//            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.toString()).build()
//        }
//    }


    /*
    This method will add accept generated ISIN from NSDL and the proof of generation
    in a document from NSDL or some email.

     */
    @POST
    @Path("addISIN/{cpProgramId}/{isin}")
    @Produces(MediaType.APPLICATION_JSON)
    fun addISIN(@PathParam("cpProgramId") cpProgramId: String,
                @PathParam("isin") isin: String
    ): Response
    {
        try
        {
//            val stx = services.invokeFlowAsync(AddIsinToCPProgramFlow::class.java, cpProgramId, isin).resultFuture.get()
//            logger.info("CP Program Issued\n\nFinal transaction is:\n\n${Emoji.renderIfSupported(stx.tx)}")
            return Response.status(Response.Status.OK).entity(getCPProgram(cpProgramId)).build()
        } catch (ex: Throwable) {
            logger.info("Exception when creating deal: ${ex.toString()}")
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.toString()).build()
        }
    }

//    @POST
//    @Path("addIPAVerificationDocs/{cpProgramId}/{docHashId}/{docStatus}")
//    fun addIPAVerificationDocs(@PathParam("cpProgramId") cpProgramId: String,
//                             @PathParam("docHashId") docHashId: String,
//                             @PathParam("docStatus") docStatus: String): Response {
//        try {
//            val indiaCPProgramJSON:IndiaCPProgramJSON = IndiaCPProgramJSON(program_id = cpProgramId, ipa_verification_request_doc_id = docHashId, status = CP_PROGRAM_FLOW_STAGES.ADD_IPA_VERI_DOC.endStatus)
//
//
//
//            val stx = services.invokeFlowAsync(CPProgramFlows::class.java, indiaCPProgramJSON, CP_PROGRAM_FLOW_STAGES.ADD_IPA_VERI_DOC).resultFuture.get()
//            logger.info("CP Program Issued\n\nFinal transaction is:\n\n${Emoji.renderIfSupported(stx.tx)}")
//            return Response.status(Response.Status.OK).build()
//        } catch (ex: Throwable) {
//            logger.info("Exception when creating deal: ${ex.toString()}")
//            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.toString()).build()
//        }
//    }
//
//    @POST
//    @Path("addIPACertifcateDoc/{cpProgramId}/{docHashId}/{docStatus}")
//    fun addIPACertifcateDoc(@PathParam("cpProgramId") cpProgramId: String,
//                               @PathParam("docHashId") docHashId: String,
//                               @PathParam("docStatus") docStatus: String): Response {
//        try {
//            val indiaCPProgramJSON:IndiaCPProgramJSON = IndiaCPProgramJSON(program_id = cpProgramId, ipa_certificate_doc_id = docHashId, status = CP_PROGRAM_FLOW_STAGES.ADD_IPA_CERT_DOC.endStatus)
//
//
//
//            val stx = services.invokeFlowAsync(CPProgramFlows::class.java, indiaCPProgramJSON, CP_PROGRAM_FLOW_STAGES.ADD_IPA_CERT_DOC).resultFuture.get()
//            logger.info("CP Program Issued\n\nFinal transaction is:\n\n${Emoji.renderIfSupported(stx.tx)}")
//            return Response.status(Response.Status.OK).build()
//        } catch (ex: Throwable) {
//            logger.info("Exception when creating deal: ${ex.toString()}")
//            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.toString()).build()
//        }
//    }
//
//    @POST
//    @Path("addCorpActionFormDoc/{cpProgramId}/{docHashId}/{docStatus}")
//    fun addCorpActionFormDoc(@PathParam("cpProgramId") cpProgramId: String,
//                            @PathParam("docHashId") docHashId: String,
//                            @PathParam("docStatus") docStatus: String): Response {
//        try {
//            val indiaCPProgramJSON:IndiaCPProgramJSON = IndiaCPProgramJSON(program_id = cpProgramId, corporate_action_form_doc_id = docHashId, status = CP_PROGRAM_FLOW_STAGES.ADD_CORP_ACT_FORM_DOC.endStatus)
//
//
//
//            val stx = services.invokeFlowAsync(CPProgramFlows::class.java, indiaCPProgramJSON, CP_PROGRAM_FLOW_STAGES.ADD_CORP_ACT_FORM_DOC).resultFuture.get()
//            logger.info("CP Program Issued\n\nFinal transaction is:\n\n${Emoji.renderIfSupported(stx.tx)}")
//            return Response.status(Response.Status.OK).build()
//        } catch (ex: Throwable) {
//            logger.info("Exception when creating deal: ${ex.toString()}")
//            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.toString()).build()
//        }
//    }
//
//    @POST
//    @Path("addAllotmentLetterDoc/{cpProgramId}/{docHashId}/{docStatus}")
//    fun addAllotmentLetterDoc(@PathParam("cpProgramId") cpProgramId: String,
//                             @PathParam("docHashId") docHashId: String,
//                             @PathParam("docStatus") docStatus: String): Response {
//        try {
//            val indiaCPProgramJSON:IndiaCPProgramJSON = IndiaCPProgramJSON(program_id = cpProgramId, allotment_letter_doc_id = docHashId, status = CP_PROGRAM_FLOW_STAGES.ADD_ALLOT_LETTER_DOC.endStatus)
//
//
//
//            val stx = services.invokeFlowAsync(CPProgramFlows::class.java, indiaCPProgramJSON, CP_PROGRAM_FLOW_STAGES.ADD_ALLOT_LETTER_DOC).resultFuture.get()
//            logger.info("CP Program Issued\n\nFinal transaction is:\n\n${Emoji.renderIfSupported(stx.tx)}")
//            return Response.status(Response.Status.OK).build()
//        } catch (ex: Throwable) {
//            logger.info("Exception when creating deal: ${ex.toString()}")
//            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.toString()).build()
//        }
//    }

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

    @GET
    @Path("getTransactionHistory/{cpProgramId}")
    @Produces(MediaType.APPLICATION_JSON)
    fun getTransactionHistory(@PathParam("cpProgramId") cpProgramId: String): Response {
        try {
            val history = CPUtils.getTransactionHistory<IndiaCommercialPaperProgram.State>(services, { programId == cpProgramId })
            return Response.status(Response.Status.OK).entity(history.map { ModelUtils.indiaCPProgramModelFromState(it) }).build()
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
    fun fetchAllCPProgram(): Response  {
        try {
            val cpProgramArray = getAllCPProgram()
            return Response.status(Response.Status.OK).entity(cpProgramArray).build()
        } catch (ex: Throwable) {
            logger.info("Exception when fetching ecp: ${ex.toString()}")
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.toString()).build()
        }
    }

    fun getAllCPProgram(): Array<IndiaCommercialPaperProgram.State>?  {
        val states = services.vaultService.linearHeadsOfType<IndiaCommercialPaperProgram.State>()
        val indiacpprogams = states.values.map { it.state.data }.toTypedArray()
        return indiacpprogams
    }

//    private fun getHistory(ref: String): Array<ContractState> {
//        val states = services.vaultService.linearHeadsOfType<IndiaCommercialPaperProgram.State>().filterValues { it.state.data.programId == ref }
//        if (states == null || states.values == null || states.values.isEmpty()) {
//            throw IndiaCPException(CPProgramError.DOES_NOT_EXIST_ERROR, Error.SourceEnum.DL_R3CORDA)
//        }
//        val stx = services.storageService.validatedTransactions.getTransaction(states.values.first()!!.ref.txhash)
//
//        val search = IndiaCPHistorySearch(services.storageService.validatedTransactions, listOf(stx!!.tx))
//        search.query = IndiaCPHistorySearch.QueryByInputStateType(followInputsOfType = IndiaCommercialPaperProgram.State::class.java)
//        val cpProgHistory : List<WireTransaction> = search.call()
//
//        return search.filterOutputStates(cpProgHistory)
//    }


    @GET
    @Path("fetchCPProgram/{cpProgramId}")
    @Produces(MediaType.APPLICATION_JSON)
    fun fetchCPProgram(@PathParam("cpProgramId") cpProgramId: String): Response {
        try {
            val cpProgram = getCPProgram(cpProgramId)
            return Response.status(Response.Status.OK).entity(cpProgram).build()
        } catch (ex: Throwable) {
            logger.info("Exception when fetching ecp: ${ex.toString()}")
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.toString()).build()
        }
    }

    private fun getCPProgram(ref: String): IndiaCommercialPaperProgram.State? {
        val states = services.vaultService.linearHeadsOfType<IndiaCommercialPaperProgram.State>().filterValues { it.state.data.programId == ref }
        return if (states.isEmpty()) null else {
            val deals = states.values.map { it.state.data }
            return if (deals.isEmpty()) null else deals[0]
        }
    }

    /*
     *  This method will upload a given set of documents into the CP Program.
     *  We can get more than one document within a given zip file.
     *
     */
    @POST
    @Path("addDocs/{cpProgramId}")
    @Consumes(MediaType.APPLICATION_JSON)
    fun addDocs(@PathParam("cpProgramId") cpProgramId: String,
                docDetails:IndiaCPDocumentDetails): Response {
        try
        {
            //Lets find the trigger type so that we rea able to
            //Trigger the correct flow.
//            var trigStage : CP_PROGRAM_FLOW_STAGES = CP_PROGRAM_FLOW_STAGES.ADD_ISIN_GEN_DOC;
//
//            val docType:IndiaCPDocumentDetails.DocTypeEnum = docDetails.docType;
//
//            if(docType == IndiaCPDocumentDetails.DocTypeEnum.DEPOSITORY_DOCS)
//            {
//                trigStage = CP_PROGRAM_FLOW_STAGES.ADD_ISIN_GEN_DOC;
//            }
//
//            if(docType == IndiaCPDocumentDetails.DocTypeEnum.IPA_CERTIFICATE_DOC)
//            {
//                trigStage = CP_PROGRAM_FLOW_STAGES.ADD_IPA_CERT_DOC;
//            }
//
//            if(docType == IndiaCPDocumentDetails.DocTypeEnum.IPA_DOCS)
//            {
//                trigStage = CP_PROGRAM_FLOW_STAGES.ADD_IPA_VERI_DOC;
//            }
//
//            if(docType == IndiaCPDocumentDetails.DocTypeEnum.CORPORATE_ACTION_FORM)
//            {
//                trigStage = CP_PROGRAM_FLOW_STAGES.ADD_CORP_ACT_FORM_DOC;
//            }
//
//
//            val stx = services.invokeFlowAsync(CPProgramFlows::class.java, cpProgramId, docDetails, trigStage).resultFuture.get()
//            logger.info("CP Program Issued\n\nFinal transaction is:\n\n${Emoji.renderIfSupported(stx.tx)}")
            return Response.status(Response.Status.OK).build()
        } catch (ex: Throwable) {
            logger.info("Exception when creating deal: ${ex.toString()}")
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.toString()).build()
        }
    }


}

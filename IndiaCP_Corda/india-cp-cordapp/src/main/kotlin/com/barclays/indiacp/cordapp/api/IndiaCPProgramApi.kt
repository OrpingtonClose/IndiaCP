package com.barclays.indiacp.cordapp.api

import com.barclays.indiacp.cordapp.contract.IndiaCommercialPaperProgram
import com.barclays.indiacp.cordapp.protocol.agreements.AddISINDocFlow
import com.barclays.indiacp.cordapp.protocol.agreements.AddISINFlow
import com.barclays.indiacp.cordapp.protocol.issuer.IssueCPProgramFlow
import com.barclays.indiacp.cordapp.utilities.CPUtils
import com.barclays.indiacp.cordapp.utilities.ErrorUtils
import com.barclays.indiacp.cordapp.utilities.ModelUtils
import com.barclays.indiacp.model.*
import net.corda.core.contracts.StateAndRef
import net.corda.core.node.ServiceHub
import net.corda.core.node.services.linearHeadsOfType
import net.corda.core.utilities.Emoji
import net.corda.core.utilities.loggerFor
import java.time.Instant
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
            logger.info("CP Program Created ${indiaCPProgramModel.programId}:${indiaCPProgramModel.name} \n\nFinal transaction is:\n\n${Emoji.renderIfSupported(stx.tx)}")

            val createdContractState = getCPProgram(indiaCPProgramModel.programId) ?: throw IndiaCPException(CPProgramError.CREATION_ERROR, Error.SourceEnum.DL_R3CORDA, "Could not fetch the newly created CPProgram from the DL");
            return Response.status(Response.Status.OK).entity(ModelUtils.indiaCPProgramModelFromState(createdContractState!!)).build()

        } catch (ex: Throwable) {
            logger.info("${CPProgramError.CREATION_ERROR}: ${ex.toString()}")
            return ErrorUtils.errorHttpResponse(ex, errorCode = CPProgramError.CREATION_ERROR)
        }
    }

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
            val contractStateRef = CPUtils.getCPProgramStateRefNonNull(services, cpProgramId)
            if (!contractStateRef.state.data.isin.isNullOrBlank()) {
                throw IndiaCPException(CPProgramError.ISIN_ALREADY_EXISTS_ERROR, Error.SourceEnum.DL_R3CORDA)
            }
            val stx = services.invokeFlowAsync(AddISINFlow::class.java, contractStateRef).resultFuture.get()
            logger.info("CP Program ${cpProgramId}: New ISIN ${isin} Added as Issued by the Depository\n\nFinal transaction is:\n\n${Emoji.renderIfSupported(stx.tx)}")

            return Response.status(Response.Status.OK).entity(ModelUtils.indiaCPProgramModelFromState(getCPProgram(cpProgramId)!!)).build()
        } catch (ex: Throwable) {
            logger.info("${CPProgramError.ISIN_CREATION_ERROR}: ${ex.toString()}")
            return ErrorUtils.errorHttpResponse(ex, errorCode = CPProgramError.ISIN_CREATION_ERROR)
        }
    }

    @POST
    @Path("closeCPProgram/{cpProgramId}")
    fun closeCPProgram(@PathParam("cpProgramId") cpProgramId: String): Response {
        try {
            //TODO: Add code here
            return Response.status(Response.Status.OK).build()
        } catch (ex: Throwable) {
            logger.info("${CPProgramError.CANCELLATION_ERROR}: ${ex.toString()}")
            return ErrorUtils.errorHttpResponse(ex, errorCode = CPProgramError.CANCELLATION_ERROR)
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
            logger.info("${CPProgramError.HISTORY_SEARCH_ERROR}: ${ex.toString()}")
            return ErrorUtils.errorHttpResponse(ex, errorCode = CPProgramError.HISTORY_SEARCH_ERROR)
        }
    }

    @GET
    @Path("getDocumentHistory/{cpProgramId}/{docType}/{docSubType}")
    @Produces(MediaType.APPLICATION_JSON)
    fun getDocumentHistory(@PathParam("cpProgramId") cpProgramId: String,
                           @PathParam("docType") docType: String,
                           @PathParam("docSubType") docSubType: String): Response {
        var history: List<IndiaCPDocumentDetails> = emptyList()
        try {
            when (docType) {
                IndiaCPDocumentDetails.DocTypeEnum.DEPOSITORY_DOCS.name -> {
                    val cpProgStatesForDocTypeTransactions = CPUtils.getDocumentTransactionHistory<IndiaCommercialPaperProgram.State, IndiaCommercialPaperProgram.Commands.AddIsinGenDoc>(services, { programId == cpProgramId })
                    history = ModelUtils.getDocumentDetailsForCPProgram(cpProgramId, cpProgStatesForDocTypeTransactions, IndiaCPDocumentDetails.DocTypeEnum.DEPOSITORY_DOCS)
                }
                IndiaCPDocumentDetails.DocTypeEnum.IPA_DOCS.name -> {
                    val cpProgStatesForDocTypeTransactions = CPUtils.getDocumentTransactionHistory<IndiaCommercialPaperProgram.State, IndiaCommercialPaperProgram.Commands.AddIPAVerification>(services, { programId == cpProgramId })
                    history = ModelUtils.getDocumentDetailsForCPProgram(cpProgramId, cpProgStatesForDocTypeTransactions, IndiaCPDocumentDetails.DocTypeEnum.IPA_DOCS)
                }
                IndiaCPDocumentDetails.DocTypeEnum.IPA_CERTIFICATE_DOC.name -> {
                    val cpProgStatesForDocTypeTransactions = CPUtils.getDocumentTransactionHistory<IndiaCommercialPaperProgram.State, IndiaCommercialPaperProgram.Commands.AddIPACertifcateDoc>(services, { programId == cpProgramId })
                    history = ModelUtils.getDocumentDetailsForCPProgram(cpProgramId, cpProgStatesForDocTypeTransactions, IndiaCPDocumentDetails.DocTypeEnum.IPA_CERTIFICATE_DOC)
                }
                IndiaCPDocumentDetails.DocTypeEnum.CORPORATE_ACTION_FORM.name -> {
                    val cpProgStatesForDocTypeTransactions = CPUtils.getDocumentTransactionHistory<IndiaCommercialPaperProgram.State, IndiaCommercialPaperProgram.Commands.AddCorpActionFormDoc>(services, { programId == cpProgramId })
                    history = ModelUtils.getDocumentDetailsForCPProgram(cpProgramId, cpProgStatesForDocTypeTransactions, IndiaCPDocumentDetails.DocTypeEnum.CORPORATE_ACTION_FORM)
                }
                else -> {
                    return ErrorUtils.errorHttpResponse(IndiaCPException("Unknown Document Type History Requested from India Commercial Paper Program Smart Contract", Error.SourceEnum.DL_R3CORDA),
                            errorCode = CPProgramError.DOC_UPLOAD_ERROR)
                }
            }
            return Response.status(Response.Status.OK).entity(history).build()
        } catch (ex: Throwable) {
            logger.info("${CPProgramError.HISTORY_SEARCH_ERROR}: ${ex.toString()}")
            return ErrorUtils.errorHttpResponse(ex, errorCode = CPProgramError.HISTORY_SEARCH_ERROR)
        }
    }

    @GET
    @Path("fetchAllCPProgram")
    @Produces(MediaType.APPLICATION_JSON)
    fun fetchAllCPProgram(): Response  {
        try {
            val cpProgramArray = getAllCPProgram()
            return Response.status(Response.Status.OK).entity(cpProgramArray?.map { ModelUtils.indiaCPProgramModelFromState(it) }).build()
        } catch (ex: Throwable) {
            logger.info("${CPProgramError.FETCH_ERROR}: ${ex.toString()}")
            return ErrorUtils.errorHttpResponse(ex, errorCode = CPProgramError.FETCH_ERROR)
        }
    }

    fun getAllCPProgram(): Array<IndiaCommercialPaperProgram.State>?  {
        val states = services.vaultService.linearHeadsOfType<IndiaCommercialPaperProgram.State>()
        val indiacpprogams = states.values.map { it.state.data }.toTypedArray()
        return indiacpprogams
    }

    @GET
    @Path("fetchCPProgram/{cpProgramId}")
    @Produces(MediaType.APPLICATION_JSON)
    fun fetchCPProgram(@PathParam("cpProgramId") cpProgramId: String): Response {
        try {
            val cpProgram = getCPProgram(cpProgramId)
            return Response.status(Response.Status.OK).entity(if(cpProgram == null) "" else ModelUtils.indiaCPProgramModelFromState(cpProgram)).build()
        } catch (ex: Throwable) {
            logger.info("${CPProgramError.FETCH_ERROR}: ${ex.toString()}")
            return ErrorUtils.errorHttpResponse(ex, errorCode = CPProgramError.FETCH_ERROR)
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
     */
    @POST
    @Path("addDocs/{cpProgramId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    fun addDocs(@PathParam("cpProgramId") cpProgramId: String,
                docDetails:IndiaCPDocumentDetails): Response {
        try
        {
            val cpProgramStateAndRef : StateAndRef<IndiaCommercialPaperProgram.State> = CPUtils.getCPProgramStateRefNonNull(services, cpProgramId)

            when (docDetails.docType) {
                IndiaCPDocumentDetails.DocTypeEnum.DEPOSITORY_DOCS -> {
                    val newStateRef = cpProgramStateAndRef.copy(state = cpProgramStateAndRef.state.copy(
                            data = cpProgramStateAndRef.state.data.copy(isinGenerationRequestDocId = docDetails.docHash + ":" + docDetails.docStatus ?:  IndiaCPDocumentDetails.DocStatusEnum.UNKNOWN.name,
                                                                        modifiedBy = docDetails.modifiedBy,
                                                                        lastModifiedDate = docDetails.lastModifiedDate?.toInstant() ?: Instant.now()
                                                                        )
                                                                    )
                                                                )
                    val stx = services.invokeFlowAsync(AddISINDocFlow::class.java, newStateRef).resultFuture.get()
                    logger.info("ISIN Request Document Uploaded & Stamped on DL \n\nFinal transaction is:\n\n${Emoji.renderIfSupported(stx.tx)}")
                }
                IndiaCPDocumentDetails.DocTypeEnum.IPA_DOCS -> {

                }
                IndiaCPDocumentDetails.DocTypeEnum.IPA_CERTIFICATE_DOC -> {

                }
                IndiaCPDocumentDetails.DocTypeEnum.CORPORATE_ACTION_FORM -> {

                }
                else -> {
                    return ErrorUtils.errorHttpResponse(IndiaCPException("Unknown Document Type Uploaded to India Commercial Paper Program Smart Contract", Error.SourceEnum.DL_R3CORDA),
                                                        errorCode = CPProgramError.DOC_UPLOAD_ERROR)
                }
            }

            return Response.status(Response.Status.OK).entity(ModelUtils.indiaCPProgramModelFromState(getCPProgram(cpProgramId)!!)).build()
        } catch (ex: Throwable) {
            logger.info("${CPProgramError.DOC_UPLOAD_ERROR}: ${ex.toString()}")
            return ErrorUtils.errorHttpResponse(ex, errorCode = CPProgramError.DOC_UPLOAD_ERROR)
        }
    }

}

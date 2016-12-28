package com.barclays.indiacp.quorum.contract;

import com.jpmorgan.cakeshop.client.model.TransactionResult;
import com.jpmorgan.cakeshop.client.model.res.APIData;
import com.jpmorgan.cakeshop.client.model.res.APIResponse;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import java.io.InputStream;

/**
 * Created by ritukedia on 23/12/16.
 */
@Path("indiacpprogram")
public class IndiaCPProgram {

    Request request;
    String userId;

    @POST
    @Path("issueCPProgram")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response issueCPProgram(IndiaCPProgram cpProgram) {
//        APIResponse<APIData<TransactionResult>, TransactionResult> res = contractApi.create(getContractCreateCommand());
//        return null;
        return Response.status(Response.Status.OK).build();

    }

    @GET
    @Path("fetchAllCPProgram")
    public Response fetchAllCPProgram() {
        return Response.status(Response.Status.OK).build();

    }

    @GET
    @Path("fetchCPProgram/{cpProgramId}")
    public Response fetchCPProgram(@PathParam("cpProgramId") String cpProgramId) {
        return Response.status(Response.Status.OK).build();

    }

    @POST
    @Path("addISIN/{cpProgramId}/{isin}")
    public Response addISIN(@PathParam("cpProgramId") String cpProgramId) {
        return Response.status(Response.Status.OK).build();

    }

    @POST
    @Path("issueCP/{cpProgramId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response issueCP(@PathParam("cpProgramId") String cpProgramId) {
        return Response.status(Response.Status.OK).build();

    }

    @POST
    @Path("addISINGenerationDocs/{cpProgramId}/{docHash}/{docStatus}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    //@UploadsDocument
    public Response addISINGenerationDocs(@PathParam("cpProgramId") String cpProgramId
            , @PathParam("docStatus") String docStatus
            , InputStream uploadedInputStream) {
        return Response.status(Response.Status.OK).build();

    }

    @Context
    public void setRequest(Request request) {
        this.request = request;
        this.userId = ""; //TODO: Parse the userId from the Request Header
    }
}

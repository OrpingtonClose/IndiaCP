package com.barclays.indiacp.dl.integration;

import com.barclays.indiacp.model.IndiaCPDocumentDetails;
import com.barclays.indiacp.model.IndiaCPIssue;
import com.barclays.indiacp.model.IndiaCPProgram;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by ritukedia on 23/12/16.
 */
@Path("indiacpissue")
public interface IndiaCPIssueApi {

    @POST
    @Path("issueCP")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response issueCP(String jsonBody);

    @POST
    @Path("addSettlementDetails/{cpIssueId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addSettlementDetails(@PathParam("cpIssueId") String cpIssueId,
                                             String jsonBody);

    @POST
    @Path("cancelCP/{cpIssueId}/{cancellationReason}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response cancelCP(@PathParam("cpIssueId") String cpIssueId,
                                 @PathParam("cancellationReason") String cancellationReason);

    @POST
    @Path("moveBeneficiaryOwnershipToInvestor/{cpIssueId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response moveBeneficiaryOwnershipToInvestor(@PathParam("cpIssueId") String cpIssueId);

    @GET
    @Path("fetchAllCP/{cpProgramId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response fetchAllCP(@PathParam("cpProgramId") String cpProgramId);

    @GET
    @Path("fetchAllCP")
    @Produces(MediaType.APPLICATION_JSON)
    public Response fetchAllCP();

    @GET
    @Path("fetchCP/{cpIssueId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response fetchCP(@PathParam("cpIssueId") String cpIssueId);

    @POST
    @Path("addDocs/{cpIssueId}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addDocs(@PathParam("cpIssueId") String cpIssueId,
                            @FormDataParam("documentDetails") IndiaCPDocumentDetails docDetails,
                            @FormDataParam("file") InputStream uploadedInputStream);


    @GET
    @Path("getTransactionHistory/{cpIssueId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTransactionHistory(@PathParam("cpIssueId") String cpIssueId);


    @GET
    @Path("getDocumentHistory/{cpIssueId}/{docType}/{docSubType}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDocumentHistory(@PathParam("cpIssueId") String cpIssueId,
                                       @PathParam("docType") String docType,
                                       @PathParam("docSubType") String docSubType);

    @Context
    public void setRequest(Request request);
}

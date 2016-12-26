package com.barclays.indiacp.dl.integration;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by ritukedia on 23/12/16.
 */
@Path("indiacpissue")
public interface IndiaCPIssue {

    @GET
    @Path("fetchAllCP")
    public Response fetchAllCP();

    @GET
    @Path("fetchCP/{cpIssueId}")
    public Response fetchCP(@PathParam("cpIssueId") String cpIssueId);

    @POST
    @Path("addDealConfirmationDocs/{cpIssueId}/{docHash}/{docStatus}")
    @Consumes(MediaType.TEXT_PLAIN)
    @UploadsDocument
    public Response addDealConfirmationDocs(@PathParam("cpIssueId") String cpIssueId
            , @PathParam("docStatus") String docStatus);

}

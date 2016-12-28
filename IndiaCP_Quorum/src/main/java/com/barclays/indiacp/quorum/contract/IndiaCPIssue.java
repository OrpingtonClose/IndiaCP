package com.barclays.indiacp.quorum.contract;

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

}

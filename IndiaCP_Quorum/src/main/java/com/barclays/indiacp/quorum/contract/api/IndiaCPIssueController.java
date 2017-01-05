package com.barclays.indiacp.quorum.contract.api;

import com.barclays.indiacp.model.IndiaCPIssue;
import com.barclays.indiacp.quorum.utils.CakeshopUtils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by ritukedia on 23/12/16.
 */
@Path("indiacpissue")
public class IndiaCPIssueController {

    @POST
    @Path("issueCPIssue")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response issueCPIssue(IndiaCPIssue indiaCPIssue) {
        String contractAddress = CakeshopUtils.createContract(this.getClass().getSimpleName().replaceFirst("Controller", ""), indiaCPIssue);
        return Response.status(Response.Status.OK).build();
    }

    @GET
    @Path("fetchAllCP")
    public Response fetchAllCP() {
        return Response.status(Response.Status.OK).build();

    }

    @GET
    @Path("fetchCP/{cpIssueId}")
    public Response fetchCP(@PathParam("cpIssueId") String cpIssueId) {

        return Response.status(Response.Status.OK).build();
    }

}

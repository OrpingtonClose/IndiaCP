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
    @Path("issueCPIssue/{cpProgAddr}")
    @Consumes(MediaType.APPLICATION_JSON)
    public String issueCPIssue(IndiaCPIssue cpIssue, @PathParam("cpProgAddr") String cpProgAddr) {
        String addr = CakeshopUtils.createContract(this.getClass().getSimpleName().replaceFirst("Controller", ""), cpIssue, cpProgAddr);
        System.out.println("Newly created contract mined at: "+addr);
        return addr;
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

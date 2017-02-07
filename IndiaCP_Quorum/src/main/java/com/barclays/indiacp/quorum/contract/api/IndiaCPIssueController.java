package com.barclays.indiacp.quorum.contract.api;

import com.barclays.indiacp.model.IndiaCPIssue;
import com.barclays.indiacp.quorum.utils.CakeshopUtils;
import com.barclays.indiacp.quorum.utils.KVDao;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by ritukedia on 23/12/16.
 */
@Path("indiacpissue")
public class IndiaCPIssueController {

    private KVDao cPIssueAddrMap = new KVDao("CPIssueAddrMap", "/home/indiacp/cakeshop/myNetwork/node1/nodedata.db");

    @POST
    @Path("issueCPIssue/{cpProgAddr}")
    @Consumes(MediaType.APPLICATION_JSON)
    public String issueCPIssue(IndiaCPIssue cpIssue, @PathParam("cpProgAddr") String cpProgAddr) {
        String addr = CakeshopUtils.createContract(this.getClass().getSimpleName().replaceFirst("Controller", ""), cpIssue, cpProgAddr);
        if (null != addr) {
            // updating cp issue lookup table
            cPIssueAddrMap.map().putIfAbsent(cpIssue.getCpTradeId(), addr);
            System.out.println("Newly created contract mined at: " + addr);
            return addr;
        }
        System.out.println("CP Issue failed!");
        return null;
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

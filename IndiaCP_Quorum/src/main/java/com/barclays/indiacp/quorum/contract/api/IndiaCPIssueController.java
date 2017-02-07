package com.barclays.indiacp.quorum.contract.api;

import com.barclays.indiacp.model.IndiaCPIssue;
import com.barclays.indiacp.model.SettlementDetails;
import com.barclays.indiacp.quorum.utils.CakeshopUtils;
import com.barclays.indiacp.quorum.utils.KVDao;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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

    @POST
    @Path("addSettlementDetails/{cpProgID}_{cpIssueID}")
    @Consumes(MediaType.APPLICATION_JSON)
    public String addSettlementDetails(SettlementDetails details, @PathParam("cpProgID") String cpProgID, @PathParam("cpIssueID") String cpIssueID) {
        String addr = (String) cPIssueAddrMap.map().get(cpIssueID);
        String detailsJSON="";
        try {
            detailsJSON = new ObjectMapper().writeValueAsString(details);
        } catch (JsonProcessingException e) {
            System.out.print("Error unpacking DTO object to get json string");
            e.printStackTrace();
        }
        // Add to solidity
        String txid = CakeshopUtils.transactContract(addr, "putSettlementDetails", new Object[] {detailsJSON});
        return txid;
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

package com.barclays.indiacp.quorum.contract.api;

import com.barclays.indiacp.model.LegalEntityCreditRatingDocument;
import com.barclays.indiacp.quorum.utils.CakeshopUtils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;

@Path("creditrating")
public class LegalEntityCreditRatingDocumentController {

    Request request;
    String userId; // is this the owner's address?

    String[] METHODS = {"fetchCPProgramTradeDetails", "fetchCPProgramDocuments", "fetchCPProgramParties", "fetchCPProgramStatus", "issueCP"}; //temporarily hardcoded

    @POST
    @Path("issueCreditRating")
    @Consumes(MediaType.APPLICATION_JSON)
    public String issueCreditRating(LegalEntityCreditRatingDocument creditRatingDocument) {
        String addr = CakeshopUtils.createContract(this.getClass().getSimpleName().replaceFirst("Controller", ""), creditRatingDocument, userId);
        System.out.println("Newly created contract mined at: " + addr);
        return addr;
    }

    @POST
    @Path("amendCreditRating")
    @Produces(MediaType.APPLICATION_JSON)
    public LegalEntityCreditRatingDocument amendCreditRating() {
        return null;
    }

    @GET
    @Path("fetchCreditRating")
    @Consumes(MediaType.APPLICATION_JSON)
    public LegalEntityCreditRatingDocument fetchCreditRating() {
        //TODO Swagger definition does not define any input
        return null;
    }

    @POST
    @Path("cancelCreditRating")
    @Consumes(MediaType.APPLICATION_JSON)
    public LegalEntityCreditRatingDocument cancelCreditRating() {
        //TODO Swagger definition does not define any input
        return null;
    }
}

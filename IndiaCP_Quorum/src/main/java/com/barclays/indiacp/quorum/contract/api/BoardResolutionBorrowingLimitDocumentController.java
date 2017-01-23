package com.barclays.indiacp.quorum.contract.api;

import com.barclays.indiacp.model.BoardResolutionBorrowingLimitDocument;
import com.barclays.indiacp.quorum.utils.CakeshopUtils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;

@Path("boardresolution")
public class BoardResolutionBorrowingLimitDocumentController {
    Request request;
    String userId; // is this the owner's address?

    @POST
    @Path("issueBoardResolution")
    @Consumes(MediaType.APPLICATION_JSON)
    public String issueCreditRating(BoardResolutionBorrowingLimitDocument borrowingLimitDocument) {
        String addr = CakeshopUtils.createContract(this.getClass().getSimpleName().replaceFirst("Controller", ""), borrowingLimitDocument, userId);
        System.out.println("Newly created contract mined at: " + addr);
        return addr;
    }

    @POST
    @Path("amendBoardResolution")
    @Produces(MediaType.APPLICATION_JSON)
    public BoardResolutionBorrowingLimitDocument amendCreditRating() {
        return null;
    }

    @GET
    @Path("fetchBoardResolution")
    @Consumes(MediaType.APPLICATION_JSON)
    public BoardResolutionBorrowingLimitDocument fetchCreditRating() {
        //TODO Swagger definition does not define any input
        return null;
    }

    @POST
    @Path("cancelBoardResolution")
    @Consumes(MediaType.APPLICATION_JSON)
    public BoardResolutionBorrowingLimitDocument cancelCreditRating() {
        //TODO Swagger definition does not define any input
        return null;
    }


}

package com.barclays.indiacp.dl.integration;

import com.barclays.indiacp.model.*;
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
@Path("legalentity")
public interface LegalEntityApi {

    @POST
    @Path("issueBoardResoultuionForBorrowingLimit")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response issueBoardResoultuionForBorrowingLimit(@FormDataParam("metadata") BoardResolutionBorrowingLimitDocument brDetails,
                                                           @FormDataParam("file") InputStream uploadedInputStream);

    @POST
    @Path("amendBoardResoultuionForBorrowingLimit")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response amendBoardResoultuionForBorrowingLimit(@FormDataParam("metadata") BoardResolutionBorrowingLimitDocument brDetails,
                                                                                        @FormDataParam("file") InputStream uploadedInputStream);
    @POST
    @Path("issueCreditRating")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response issueCreditRating(@FormDataParam("metadata") LegalEntityCreditRatingDocument crDetails,
                                                                                  @FormDataParam("file") InputStream uploadedInputStream);


    @POST
    @Path("amendCreditRating")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response amendCreditRating(@FormDataParam("metadata") LegalEntityCreditRatingDocument crDetails,
                                                                                  @FormDataParam("file") InputStream uploadedInputStream);

    @Context
    public void setRequest(Request request);
}

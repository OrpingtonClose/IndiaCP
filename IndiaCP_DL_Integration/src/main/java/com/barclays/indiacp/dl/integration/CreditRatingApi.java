package com.barclays.indiacp.dl.integration;

import com.barclays.indiacp.dl.utils.DLAttachmentUtils;
import com.barclays.indiacp.dl.utils.DLUtils;
import com.barclays.indiacp.model.IndiaCPDocumentDetails;
import com.barclays.indiacp.model.LegalEntityCreditRatingDocument;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.ws.rs.*;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import java.io.InputStream;

/**
 * Created by ritukedia on 23/12/16.
 */
@Path("creditrating")
public class CreditRatingApi {
    WebTarget dlRestEndPoint;
    String resourcePath;

    public CreditRatingApi() {
        this.resourcePath = "creditrating";
        this.dlRestEndPoint = DLUtils.getDLRestEndPoint();
    }

    @POST
    @Path("issueCreditRating")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response issueCreditRating(
                                      @FormDataParam("creditRatingDetails") LegalEntityCreditRatingDocument crDetails,
                                      @FormDataParam("file") InputStream uploadedInputStream)
    {
        //Upload attachment to DL
        String docHash = DLAttachmentUtils.getInstance().uploadAttachment(uploadedInputStream);

        //Set the Uploaded DocHash on the Contract State to establish proof of existence of the document on the DL
        crDetails.setDocHash(docHash);

        Response response = DLUtils.getDLRestEndPoint()
                            .path(resourcePath + "/" + "issueCreditRating").request().post(Entity.json(DLUtils.getJSONString(crDetails)));
        return response;

    }

    @POST
    @Path("amendCreditRating")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response amendCreditRating(
            @FormDataParam("creditRatingDetails") LegalEntityCreditRatingDocument crDetails,
            @FormDataParam("file") InputStream uploadedInputStream)
    {
        //Upload attachment to DL
        String docHash = DLAttachmentUtils.getInstance().uploadAttachment(uploadedInputStream);

        //Set the Uploaded DocHash on the Contract State to establish proof of existence of the document on the DL
        crDetails.setDocHash(docHash);

        Response response = DLUtils.getDLRestEndPoint()
                .path(resourcePath + "/" + "amendCreditRating").request().post(Entity.json(DLUtils.getJSONString(crDetails)));
        return response;

    }

    @GET
    @Path("fetchCreditRating")
    @Produces(MediaType.APPLICATION_JSON)
    public Response fetchCreditRating() {
        Response response = dlRestEndPoint.path(resourcePath + "/" + "fetchCreditRating").request().get();

        return response;
    }

    @GET
    @Path("cancelCreditRating")
    @Produces(MediaType.APPLICATION_JSON)
    public Response cancelCreditRating() {
        Response response = dlRestEndPoint.path(resourcePath + "/" + "cancelCreditRating").request().get();

        return response;
    }
}

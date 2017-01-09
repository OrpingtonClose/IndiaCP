package com.barclays.indiacp.dl.integration;

import com.barclays.indiacp.dl.utils.DLAttachmentUtils;
import com.barclays.indiacp.dl.utils.DLUtils;
import com.barclays.indiacp.model.BoardResolutionBorrowingLimitDocument;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.ws.rs.*;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;

/**
 * Created by ritukedia on 23/12/16.
 */
@Path("boardresolution")
public class BorrowingLimitBoardResolutionApi {
    WebTarget dlRestEndPoint;
    String resourcePath;

    public BorrowingLimitBoardResolutionApi() {
        this.resourcePath = "boardresolution";
        this.dlRestEndPoint = DLUtils.getDLRestEndPoint();
    }

    @POST
    @Path("issueBoardResolution")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response issueBoardResolution(
                                      @FormDataParam("boardResolutionDetails") BoardResolutionBorrowingLimitDocument brDetails,
                                      @FormDataParam("file") InputStream uploadedInputStream)
    {
        //Upload attachment to DL
        String docHash = DLAttachmentUtils.getInstance().uploadAttachment(uploadedInputStream);

        //Set the Uploaded DocHash on the Contract State to establish proof of existence of the document on the DL
        brDetails.setDocHash(docHash);

        Response response = DLUtils.getDLRestEndPoint()
                            .path(resourcePath + "/" + "issueBoardResolution").request().post(Entity.json(DLUtils.getJSONString(brDetails)));
        return response;

    }

    @POST
    @Path("amendBoardResolution")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response amendBoardResolution(
            @FormDataParam("boardResolutionDetails") BoardResolutionBorrowingLimitDocument brDetails,
            @FormDataParam("file") InputStream uploadedInputStream)
    {
        //Upload attachment to DL
        String docHash = DLAttachmentUtils.getInstance().uploadAttachment(uploadedInputStream);

        //Set the Uploaded DocHash on the Contract State to establish proof of existence of the document on the DL
        brDetails.setDocHash(docHash);

        Response response = DLUtils.getDLRestEndPoint()
                .path(resourcePath + "/" + "amendBoardResolution").request().post(Entity.json(DLUtils.getJSONString(brDetails)));
        return response;

    }

    @GET
    @Path("fetchBoardResolution")
    @Produces(MediaType.APPLICATION_JSON)
    public Response fetchBoardResolution() {
        Response response = dlRestEndPoint.path(resourcePath + "/" + "fetchBoardResolution").request().get();

        return response;
    }

    @GET
    @Path("cancelBoardResolution")
    @Produces(MediaType.APPLICATION_JSON)
    public Response cancelBoardResolution() {
        Response response = dlRestEndPoint.path(resourcePath + "/" + "cancelBoardResolution").request().get();

        return response;
    }
}

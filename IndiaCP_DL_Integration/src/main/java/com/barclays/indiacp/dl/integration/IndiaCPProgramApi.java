package com.barclays.indiacp.dl.integration;

import com.barclays.indiacp.model.IndiaCPDocumentDetails;
import com.barclays.indiacp.model.IndiaCPProgram;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.zip.ZipFile;

/**
 * Created by ritukedia on 23/12/16.
 */
@Path("indiacpprogram")
public interface IndiaCPProgramApi {

    @POST
    @Path("issueCPProgram")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response issueCPProgram(String jsonBody);

    @GET
    @Path("fetchAllCPProgram")
    @Produces(MediaType.APPLICATION_JSON)
    public Response fetchAllCPProgram();

    @GET
    @Path("fetchCPProgram/{cpProgramId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response fetchCPProgram(@PathParam("cpProgramId") String cpProgramId);

    @POST
    @Path("addISIN/{cpProgramId}/{isin}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addISIN(@PathParam("cpProgramId") String cpProgramId);

    @POST
    @Path("addDocs/{cpProgramId}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addDocs(@PathParam("cpProgramId") String cpProgramId,
                            @FormDataParam("documentDetails") ArrayList<IndiaCPDocumentDetails> docDetails,
                            @FormDataParam("file") InputStream uploadedInputStream);

    @Context
    public void setRequest(Request request);
}

package com.barclays.indiacp.dl.integration;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;
import java.io.InputStream;
import java.util.zip.ZipFile;

/**
 * Created by ritukedia on 23/12/16.
 */
@Path("indiacpprogram")
public interface IndiaCPProgram {

    @POST
    @Path("issueCPProgram")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response issueCPProgram(String jsonBody);

    @GET
    @Path("fetchAllCPProgram")
    public Response fetchAllCPProgram();

    @GET
    @Path("fetchCPProgram/{cpProgramId}")
    public Response fetchCPProgram(@PathParam("cpProgramId") String cpProgramId);

    @POST
    @Path("addISIN/{cpProgramId}/{isin}")
    public Response addISIN(@PathParam("cpProgramId") String cpProgramId);

    @POST
    @Path("issueCP/{cpProgramId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response issueCP(@PathParam("cpProgramId") String cpProgramId);

    @POST
    @Path("addISINGenerationDocs/{cpProgramId}/{docHash}/{docStatus}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    //@UploadsDocument
    public Response addISINGenerationDocs(@PathParam("cpProgramId") String cpProgramId
                                          , @PathParam("docStatus") String docStatus
                                          , InputStream uploadedInputStream);
}

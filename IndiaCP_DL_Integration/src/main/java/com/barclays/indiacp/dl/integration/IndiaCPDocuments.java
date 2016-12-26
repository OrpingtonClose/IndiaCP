package com.barclays.indiacp.dl.integration;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.zip.ZipFile;

/**
 * Created by ritukedia on 24/12/16.
 */
@Path("indiacpdocuments")
public class IndiaCPDocuments {

    @POST
    @Path("generateISINDocuments")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response generateISINDocuments(String jsonBody) {

        //Generate documents to be sent to NSDL to generate ISIN
        ZipFile zip = null; //TODO: Use Doc Generation Utility

        //return base64 encoded string

        return Response.status(Response.Status.OK).build();
    }

    @POST
    @Path("generateIPADocuments")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response generateIPADocuments(String jsonBody) {

        //Generate documents to be sent to IPA for verification
        ZipFile zip = null; //TODO: Use Doc Generation Utility

        //return base64 encoded string

        return Response.status(Response.Status.OK).build();
    }

    @POST
    @Path("generateDealConfirmationDocument")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response generateDealConfirmationDocument(String jsonBody) {

        //Generate Deal Confirmation documents to be sent to Investor
        ZipFile zip = null; //TODO: Use Doc Generation Utility

        //return base64 encoded string

        return Response.status(Response.Status.OK).build();
    }
}
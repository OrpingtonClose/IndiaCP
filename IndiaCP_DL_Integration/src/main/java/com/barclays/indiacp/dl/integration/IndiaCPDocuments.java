package com.barclays.indiacp.dl.integration;

import com.barclays.indiacp.dl.utils.DLConfig;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.util.logging.Logger;
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

//    @POST
//    @Path("uploadDoc")
//    @Consumes(MediaType.MULTIPART_FORM_DATA)
//    public Response uploadDoc(InputStream uploadedInputStream){
//
//        Logger logger = Logger.getLogger(IndiaCPProgram.class.getName());
//        Client jerseyClient = ClientBuilder.newClient();
//        //Client jerseyClient = ClientBuilder.newClient(new ClientConfig().register(org.glassfish.jersey.jsonp.internal.JsonProcessingAutoDiscoverable.class));
//        //Feature feature = new LoggingFeature(logger, Level.INFO, null, null);
//        //jerseyClient.register(feature);
//        WebTarget dlRestEndPoint = jerseyClient.target(DLConfig.DLConfigInstance().getDLRestEndpoint());
//
//        StreamDataBodyPart streamDataBodyPart = new StreamDataBodyPart("myfile", uploadedInputStream, "myfile", MediaType.APPLICATION_OCTET_STREAM_TYPE);
//
//        final Response attachmentResponse = dlRestEndPoint.path(DLConfig.DLConfigInstance().getDLUploadAttachmentPath())
//                .request()
//                .post(Entity.entity(streamDataBodyPart, MediaType.MULTIPART_FORM_DATA));
//
//        String docHash = attachmentResponse.getEntity().toString(); //TODO: extract hash from attachmentResponse
//        System.out.println("file uploaded to DL");
//        return Response.status(Response.Status.OK).build();
//
//    }
}
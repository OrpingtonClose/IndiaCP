package com.barclays.indiacp.dl.integration;

import com.barclays.indiacp.dl.utils.DLConfig;
import com.barclays.indiacp.model.IndiaCPDocumentDetails;
import com.barclays.indiacp.model.IndiaCPProgram;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.ws.rs.*;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.zip.ZipFile;
import com.barclays.indiacp.dl.utils.DLConfig;
import com.barclays.indiacp.model.IndiaCPDocumentDetails;
import com.barclays.indiacp.model.IndiaCPProgram;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

//import org.json.simple.parser.JSONParser;


/**
 * Created by ritukedia on 24/12/16.
 */
@Path("test")
public class Test {

    WebTarget dlRestEndPoint;
    @GET
    @Path("getDocs/{docHash}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response testDocs(@PathParam("docHash") String docHash) {


        Response attachmentResponse = dlRestEndPoint.path("http://localhost:10005/attachments/")
                .request(MediaType.APPLICATION_OCTET_STREAM)
                .put(Entity.text(docHash));

        return Response.status(Response.Status.OK).build();
    }

    @POST
    @Path("addDocs")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addDocs(
                            @FormDataParam("documentDetails") IndiaCPDocumentDetails docDetails,
                            @FormDataParam("file") InputStream uploadedInputStream) {
        System.out.println(docDetails);

        return Response.status(Response.Status.OK).entity(docDetails).build();
    }

}
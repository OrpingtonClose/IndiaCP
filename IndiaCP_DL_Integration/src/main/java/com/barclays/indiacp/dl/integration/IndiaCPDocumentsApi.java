package com.barclays.indiacp.dl.integration;

import com.barclays.indiacp.dl.utils.DLConfig;
import jdk.nashorn.internal.parser.JSONParser;

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
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.Locale;
import java.util.logging.Logger;
import java.util.zip.ZipFile;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.json.simple.JSONObject;
//import org.json.simple.parser.JSONParser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.json.simple.parser.ParseException;


/**
 * Created by ritukedia on 24/12/16.
 */
@Path("indiacpdocuments")
public class IndiaCPDocumentsApi {

    @POST
    @Path("generateISINDocuments")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response generateISINDocuments(String jsonBody) {

        //for file storage
        String path = "E:\\ISINDoc.pdf";

         try {
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
            Roles roleObj = gson.fromJson(jsonBody.toString(), Roles.class);
            Roles.CP cpDetails = roleObj.cp;
            Roles.Investor invDetails = roleObj.investor;
            Roles.IPA ipaDetails = roleObj.ipa;
            Roles.NSDL nsdlDetails = roleObj.nsdl;


            Font mainFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
                    Font.BOLD);
            Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
                    Font.BOLD);
            Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
                    Font.BOLD);
            Font normal = new Font(Font.FontFamily.TIMES_ROMAN, 12);
            Font smallFont = new Font(Font.FontFamily.TIMES_ROMAN, 10);


            DateFormat dtf = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
            //allotmentDate = new Date();
            String allotmentDateStr = dtf.format(cpDetails.allotmentDate);
            String maturityDateStr = dtf.format(cpDetails.maturityDate);


            // Instantiation of document object
            Document document = new Document(PageSize.A4, 50, 50, 50, 50);

            // Creation of PdfWriter object
             //for file storage
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(path));
             //OutputStream os = null;
            // PdfWriter writer = PdfWriter.getInstance(document, os);

            document.open();

            // Creation of paragraph object
            Paragraph heading = new Paragraph("Letter of Intent for Commercial Paper", mainFont);
            heading.setAlignment(Element.ALIGN_CENTER);
            document.add(heading);

            document.add(new Paragraph("\n\n"));


            DateFormat dtf1 = new SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH);
            String today = dtf1.format(new Date());

            document.add(new Paragraph("Date: " + today, normal));

            document.add(new Paragraph("\n"));


            Paragraph title11 = new Paragraph("The Managing Director", smallBold);
            document.add(title11);
            // Listing 7. Creation of list object

            Paragraph nsdlAddress = new Paragraph(String.format(nsdlDetails.nsdlAddress.replace(',', "\n".charAt(0))));
            nsdlAddress.setIndentationLeft(300);
            document.add(nsdlAddress);
            document.add(new Paragraph("\n"));

            Paragraph docSubject = new Paragraph("Sub: Admission of Commercial Paper", smallBold);
            document.add(docSubject);
            document.add(new Paragraph("\n"));
            Paragraph docAddressedPerson = new Paragraph("Dear Sir,", normal);
            document.add(docAddressedPerson);

            document.add(new Paragraph("\n"));

            String body = "We are pleased to inform you that our company has decided to offer the following Commercial Paper " +
                    "as eligible securities under the Depositories Act, 1996. We confirm that these instruments will be governed" +
                    "by the terms and conditions indicated in the tripartite agreement entered between " + cpDetails.issuerName
                    + ", " + cpDetails.secondParty + ", and NSDL. The details of the instrument are as follows:";
            document.add(new Paragraph(body, normal));

            document.add(new Paragraph("\n"));

            PdfPTable t = new PdfPTable(6);


            PdfPCell c1 = new PdfPCell(new Phrase("Sr. No.", smallFont));
            t.addCell(c1);
            PdfPCell c2 = new PdfPCell(new Phrase("Description of the Security", smallFont));
            t.addCell(c2);
            PdfPCell c3 = new PdfPCell(new Phrase("Allotment Date", smallFont));
            t.addCell(c3);
            PdfPCell c4 = new PdfPCell(new Phrase("Maturity Date", smallFont));
            t.addCell(c4);
            PdfPCell c5 = new PdfPCell(new Phrase("Issue Value", smallFont));
            t.addCell(c5);
            PdfPCell c6 = new PdfPCell(new Phrase("Redemption Value", smallFont));
            t.addCell(c6);
            t.addCell(new Phrase("1", smallFont));
            t.addCell(new Phrase(cpDetails.description, smallFont));
            t.addCell(new Phrase(allotmentDateStr, smallFont));
            t.addCell(new Phrase(maturityDateStr, smallFont));
            t.addCell(new Phrase(cpDetails.issueValue, smallFont));
            t.addCell(new Phrase(cpDetails.redemptionValue, smallFont));
            t.setTotalWidth(PageSize.A4.getWidth() - 100);
            t.setLockedWidth(true);
            document.add(t);

            String body2 = "We understand that NSDL will not levy any charges to the investors holdiing securities  in the " +
                    "Demat form. We agree to pay a sum of " + cpDetails.tax + " + ST per calendar year towards admission / electronic credit" +
                    " of these securities in the NSDL system.";

            document.add(new Paragraph(body2, normal));

            document.add(new Paragraph("\n"));

            String body3 = "We understand that for the purpose of allotment and redemption an account has to be opened by IPA " +
                    "with a depository participant of NSDL. The same is being communicated to NSDL (in the MCF) and also to the holder of the " +
                    "Commercial Paper.";
            document.add(new Paragraph(body3, normal));

            document.add(new Paragraph("\n\n"));

            document.add(new Paragraph("Yours faithfully", smallBold));

            document.add(new Paragraph("For " + cpDetails.issuerName, smallBold));

            document.add(new Paragraph("(Authorized Signatories)", smallBold));
            document.add(new Paragraph("\n\n"));


            document.newPage();
            document.add(new Paragraph("MASTER FILE CREATION FORM FOR COMMERCIAL PAPER", normal));
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("Full name of the Company : " + cpDetails.issuerName.toUpperCase(), smallBold));
            document.add(new Paragraph("Corporate Identity Number (CIN):" + cpDetails.CIN, normal));
            document.add(new Paragraph("Whether company has already signed agreement with NSDL for any other instrument ? " +
                    "(YES/NO) : " + cpDetails.NSDLHistory, normal));

            document.add(new Paragraph("Section A", smallBold));

            document.add(new Paragraph("Address of the Regd. office including telephone, fax nos. and email " +
                    "addresses : \n" + cpDetails.issuerAddress, normal));
            document.add(new Paragraph("Type of entity :" + cpDetails.entityType, smallBold));
            document.close();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
         } catch (DocumentException e) {
             e.printStackTrace();
         } catch (IOException e) {
            e.printStackTrace();
        }
        String pdfFileBase64 = "";
        try
        {
            //for file storage

       // File pdfFile = new File(path);
            pdfFileBase64 = encodeFileToBase64Binary(path);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return Response.ok(pdfFileBase64, MediaType.TEXT_PLAIN)
                .build();
        //return Response.status(Response.Status.OK).build();
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

    public static String encodeFileToBase64Binary(String fileName) throws IOException {
        File file = new File(fileName);
        byte[] encoded = Base64.encodeBase64(FileUtils.readFileToByteArray(file));
        return new String(encoded, StandardCharsets.US_ASCII);
    }

//    @POST
//    @Path("uploadDoc")
//    @Consumes(MediaType.MULTIPART_FORM_DATA)
//    public Response uploadDoc(InputStream uploadedInputStream){
//
//        Logger logger = Logger.getLogger(IndiaCPProgramApi.class.getName());
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
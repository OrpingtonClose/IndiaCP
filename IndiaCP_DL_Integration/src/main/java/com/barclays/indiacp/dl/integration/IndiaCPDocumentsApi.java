package com.barclays.indiacp.dl.integration;

import com.barclays.indiacp.dl.utils.DLAttachmentUtils;
import com.barclays.indiacp.dl.utils.DLConfig;
import com.barclays.indiacp.model.IndiaCPDocumentDetails;
import com.barclays.indiacp.model.IndiaCPProgram;
import jdk.nashorn.internal.parser.JSONParser;

import javax.ws.rs.*;
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

    @GET
    @Path("generateISINDocuments")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response generateISINDocuments(String inputJSON) {

        String pdfFileBase64 = "";

         try {
             Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
             Roles roleObj = gson.fromJson(inputJSON.toString(), Roles.class);
             Roles.CP cpDetails = roleObj.cp;

             Roles.Investor invDetails = roleObj.investor;
             Roles.IPA ipaDetails = roleObj.ipa;
             Roles.NSDL nsdlDetails = roleObj.nsdl;

             final File tempFile = File.createTempFile("IPA", ".pdf");
             String path = tempFile.getPath();

             tempFile.deleteOnExit();



             Font mainFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
                     Font.BOLD);
             Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
                     Font.BOLD);
             Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
                     Font.BOLD);
             Font normal = new Font(Font.FontFamily.TIMES_ROMAN, 12);

             Font smallFont = new Font(Font.FontFamily.TIMES_ROMAN, 10);


             DateFormat dtf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
             DateFormat dtf1 = new SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH);
             String todayStr = dtf1.format(new Date());
             //String today = dtf1.format(new Date());

               /* String dueDateStr = dtf.format(cpDetails.matDate);
                String validityStr = dtf.format(cpDetails.ratingValidityDate);
                String dateOfRatingStr = dtf.format(cpDetails.dateOfRating);
                String effectiveDateForRatingStr = dtf.format(cpDetails.effectiveDateOfRating);
                String valueDateStr = dtf.format(cpDetails.valueDate);
                String dateOfContractStr = dtf.format(cpDetails.dateOfContract);
                String tradeDateStr = dtf.format(cpDetails.tradeDate);*/

             String dueDateStr = cpDetails.matDate;
             String validityStr = cpDetails.ratingValidityDate;
             String dateOfRatingStr = cpDetails.dateOfRating;
             String effectiveDateForRatingStr = cpDetails.effectiveDateOfRating;
             String valueDateStr = cpDetails.valueDate;
             String dateOfContractStr = cpDetails.dateOfContract;
             String tradeDateStr = cpDetails.tradeDate;
             // Instantiation of document object
             Document document = new Document(PageSize.A4, 50, 50, 50, 50);

             // Creation of PdfWriter object
             PdfWriter writer = PdfWriter.getInstance(document,
                     new FileOutputStream(path));

             document.open();

             // Creation of paragraph object
             Paragraph heading = new Paragraph("DEAL CONFIRMATION NOTE / CONTRACT NOTE", mainFont);
             heading.setAlignment(Element.ALIGN_CENTER);
             document.add(heading);
             document.add(new Paragraph("\n"));

             document.add(new Paragraph("Date of contract : " + dateOfContractStr, normal));
             document.add(new Paragraph("CP (Maturity Value) : " + cpDetails.maturityValue, normal));
             document.add(new Paragraph("Due Date : " + dueDateStr, normal));
             document.add(new Paragraph("Price : " + cpDetails.price, normal));
             document.add(new Paragraph("Disc. rate : " + cpDetails.discRate, normal));
             document.add(new Paragraph("Issue Reference : " + cpDetails.issueRef, normal));
             document.add(new Paragraph("ISIN Code : " + cpDetails.isinCode, normal));
             document.add(new Paragraph("Credit Rating : " + cpDetails.creditRating, normal));
             document.add(new Paragraph("Issued by : " + cpDetails.ratingIssuedBy, normal));
             document.add(new Paragraph("Date of rating : " + dateOfRatingStr, normal));
             document.add(new Paragraph("Validity : " + validityStr, normal));
             document.add(new Paragraph("Effective date for rating : " + effectiveDateForRatingStr, normal));
             document.add(new Paragraph("For amount : " + cpDetails.issueValue, normal));
             document.add(new Paragraph("Conditions (if any) : " + cpDetails.conditions, normal));
             document.add(new Paragraph("Credit Support (if any) : " + cpDetails.creditSupport, normal));
             document.add(new Paragraph("Description of instrument : " + cpDetails.desc, normal));
             document.add(new Paragraph("Amount : " + cpDetails.totalAmount, normal));
             document.add(new Paragraph("Issued by : " + cpDetails.issuerName, normal));
             document.add(new Paragraph("\n"));
             document.add(new Paragraph("In favor of : " + invDetails.investorName, smallBold));
             document.add(new Paragraph("\n"));
             document.add(new Paragraph("Issuing and Paying Agent : " + ipaDetails.ipaName, normal));
             document.add(new Paragraph("\n"));
             document.add(new Paragraph("Issuer's liability under the CP will continue beyond the due date, in case the CP " +
                     "is not paid on due date, even if the CP in D-MAT form is extinguished on due date", normal));
             document.add(new Paragraph("\n"));
             document.add(new Paragraph("Seller of CP : " + cpDetails.issuerName, normal));
             document.add(new Paragraph("\n"));
             document.add(new Paragraph("Purchaser of CP : " + invDetails.investorName, smallBold));
             document.add(new Paragraph("\n\n"));


             document.add(new Paragraph("SETTLEMENT INSTRUCTIONS", subFont));
             document.add(new Paragraph("Value Date : " + valueDateStr, normal));
             document.add(new Paragraph("Please credit to : " + cpDetails.creditTo + " " + cpDetails.currentAccNo + " " + cpDetails.IFSCCode, normal));
             document.add(new Paragraph("Please deliver to : " + invDetails.investorName, smallBold));
             document.add(new Paragraph("\n"));
             document.add(new Paragraph("DP Name : " + invDetails.dpName, normal));
             document.add(new Paragraph("Client ID : " + invDetails.clientId, normal));
             document.add(new Paragraph("DP ID : " + invDetails.dpId, normal));
             document.add(new Paragraph("\n"));
             document.add(new Paragraph("Market conventions : " + cpDetails.marketConventions, normal));
             document.add(new Paragraph("\n"));
             document.add(new Paragraph("The deal is done by : ", smallBold));
             document.add(new Paragraph("(ON BEHALF OF SELLER) : " + cpDetails.issuerContactPerson, normal));
             document.add(new Paragraph("(ON BEHALF OF PURCHASER) : " + invDetails.contactPerson, normal));
             document.add(new Paragraph("(ON TRADE DATE) : " + tradeDateStr, normal));
             document.add(new Paragraph("No recourse is available to the purchaser of CP against previous holders of the CP. : ", normal));
             document.add(new Paragraph("This contract note is executed by \n", normal));
             document.add(new Paragraph("ON BEHALF OF : ", smallBold));
             document.add(new Paragraph("\n\n"));

             document.add(new Paragraph(cpDetails.issuerName, smallBold));
             document.add(new Paragraph("\n\n"));
             document.add(new Paragraph("ON BEHALF OF \n\n", smallBold));
             document.add(new Paragraph(invDetails.investorName, smallBold));


             document.newPage();
             document.add(new Paragraph(todayStr, normal));
             document.add(new Paragraph("\n"));
             document.add(new Paragraph(ipaDetails.ipaName, smallBold));
             document.add(new Paragraph(ipaDetails.ipaAddress, normal));
             document.add(new Paragraph("\n"));
             document.add(new Paragraph("Sub: Issuance of Commercial Paper in Dematerialised form.", smallBold));
             document.add(new Paragraph("\n"));
             document.add(new Paragraph("We refer to the Issuing and Paying Agency Agreement executed with you in" +
                     " connection with the issue of Commercial Paper in dematerialised form as per the guidelines issued" +
                     " by Reserve Bank of India\nIn connection with the above, we would like to confirm that:\n"));
             document.add(new Paragraph("a) We are eligible to issue commercial Paper as per the norms fixed by " +
                     "Reserve Bank of India.\n"));
             document.add(new Paragraph("b) The amount of " + cpDetails.maturityValue + " proposed to be raised by us by " +
                     "issue of Commercial Paper including the amounts already raised is within the amounts mentioned" +
                     "by " + cpDetails.ratingIssuedBy + " vide their letter dated " + dateOfRatingStr + " and amounts" +
                     " approved by the Board.\n"));
             document.add(new Paragraph("c) In terms of (i) Net worth (ii) working capital facilities sanctioned by" +
                     " banks/financial institutions\n\n"));
             document.add(new Paragraph("For " + cpDetails.issuerName + "\n\n", smallBold));
             document.add(new Paragraph("Authorised Signatories\n\n", smallBold));


             document.newPage();
             Paragraph title = new Paragraph("ANNEXURE II", subFont);
             title.setAlignment(Element.ALIGN_CENTER);
             document.add(title);
             document.add(new Paragraph("\n\n"));
             document.add(new Paragraph(todayStr, normal));
             document.add(new Paragraph("To\nThe Chief General Manager\nFinancial Markets Department\n" +
                     "Reserve Bank of India (RBI)\nCentral Ofiice\nMumbai – 400 001\n\n", normal));
             document.add(new Paragraph("Through \n", normal));
             document.add(new Paragraph(ipaDetails.ipaName, smallBold));
             document.add(new Paragraph(ipaDetails.ipaAddress + "\n", normal));
             document.add(new Paragraph("Dear Sir, \n", normal));
             document.add(new Paragraph("Issue of Commercial Paper", smallBold));
             document.add(new Paragraph("In terms of the Guidelines for issuance of Commercial paper issued " +
                     "by the Reserve Bank of India (RBI) dated August 19,2003 we have issued Commercial Paper as per" +
                     " details furnished hereunder : \n", normal));
             document.add(new Paragraph("i)  Name of issuer :  " + cpDetails.issuerName, normal));
             document.add(new Paragraph("ii) Registered Office and Address :  " + cpDetails.issuerAddress, normal));
             document.add(new Paragraph("iii) Business Activity : " + cpDetails.businessActivity, normal));
             document.add(new Paragraph("iv) Name/s of Stock Exchange/s with whom shares of the issuer \n" +
                     "are listed (if applicable)\n : " + cpDetails.stockExchange, normal));
             document.add(new Paragraph("v) Tangible net worth as per latest audited balance sheet : \n" +
                     cpDetails.netWorth, smallBold));
             document.add(new Paragraph("vi) Total Working Capital Limit : " + cpDetails.workingCapitalLimit, smallBold));
             document.add(new Paragraph("vii) Outstanding  Bank Borrowings : " + cpDetails.outstandingBankBorrowing, normal));
             document.add(new Paragraph("viii) (a) Details of Commercial Paper ", normal));
             document.add(new Paragraph("Issued (Face Value) : " + cpDetails.faceValue, normal));
             document.add(new Paragraph("Date of Issue : " + cpDetails.valueDate, normal));
             document.add(new Paragraph("Date of Maturity : " + cpDetails.matDate, normal));
             document.add(new Paragraph("Amount : " + cpDetails.maturityValue, normal));
             document.add(new Paragraph("Rate : " + cpDetails.discRate, normal));
             document.add(new Paragraph("b) Amount of CP outstanding (Face value) Including the present issue (Including" +
                     " Short term Debts) : " + cpDetails.amountOfCPOutstanding, normal));
             document.add(new Paragraph("ix) Rating(s) obtained from the Credit Rating Information Services of India " +
                     "Ltd. (CRISIL) Or any other agency approved by the Reserve Bank of India (RBI) : "
                     + cpDetails.creditRating, normal));
             document.add(new Paragraph("For and on behalf of" + cpDetails.effectiveDateOfRating, normal));
             document.add(new Paragraph("\n\n", normal));
             document.add(new Paragraph(cpDetails.issuerName, normal));


             document.newPage();
             Paragraph title1 = new Paragraph("Annexure- VI", subFont);
             title1.setAlignment(Element.ALIGN_CENTER);
             document.add(title1);
             Paragraph subTitle = new Paragraph("ISSUE OF COMMERCIAL PAPER (C P )", subFont);
             subTitle.setAlignment(Element.ALIGN_CENTER);
             document.add(subTitle);
             Paragraph subTitle1 = new Paragraph("LETTER OF OFFER", subFont);
             subTitle1.setAlignment(Element.ALIGN_CENTER);
             document.add(subTitle1);

             document.add(new Paragraph("PART I", smallBold));
             document.add(new Paragraph("PROPOSED DATE OF ISSUE : " + cpDetails.valueDate, normal));
             // document.add(new Paragraph("TENOR (in days) : "
             //         + dateDiff(cpDetails.valueDate.toString(), cpDetails.maturityDate.toString()), normal));
             document.add(new Paragraph("DUE DATE : " + cpDetails.matDate, normal));
             document.add(new Paragraph("ISSUE REFERENCE : " + cpDetails.issueRef, normal));
             document.add(new Paragraph("ISIN CODE : " + cpDetails.isinCode, normal));
             document.add(new Paragraph("ISSUE SIZE (Maturity Value) : " + cpDetails.maturityValue, normal));
             document.add(new Paragraph("CREDIT RATING : " + cpDetails.creditRating, normal));
             document.add(new Paragraph("ISSUED BY : " + cpDetails.ratingIssuedBy, normal));
             document.add(new Paragraph("DATE OF RATING : " + cpDetails.effectiveDateOfRating, normal));
             document.add(new Paragraph("VALIDITY : " + cpDetails.ratingValidityDate, normal));
             document.add(new Paragraph("FOR AMOUNT : " + cpDetails.issueValue, normal));
             document.add(new Paragraph("CONDITIONS (if any) : " + cpDetails.conditions, normal));
             document.add(new Paragraph("CREDIT SUPPORT  (if any) : " + cpDetails.creditSupport, normal));
             document.add(new Paragraph("DESCRIPTION OF INSTRUMENT : " + cpDetails.desc, normal));
             document.add(new Paragraph("AMOUNT : " + cpDetails.totalAmount, normal));
             document.add(new Paragraph("ISSUED BY : " + cpDetails.issuerName, normal));
             document.add(new Paragraph("\n"));
             document.add(new Paragraph("IN FAVOUR OF : " + invDetails.investorName, smallBold));
             document.add(new Paragraph("\n"));
             document.add(new Paragraph("ISSUER OF CP / HOLDERS OF CP : " + cpDetails.issuerName, normal));
             document.add(new Paragraph("Issuing and Paying Agent : " + ipaDetails.ipaName, normal));
             document.add(new Paragraph("\n"));
             document.add(new Paragraph("MARKET CONVENTIONS : " + cpDetails.marketConventions, normal));
             document.add(new Paragraph("SUPPORTING BOARD RESOLUTION : " + cpDetails.boardResolutionDate, normal));
             document.add(new Paragraph("TOTAL CP OUTSTANDING (as on date) (Including Short term Debts)" +
                     " : " + cpDetails.amountOfCPOutstanding, normal));


             document.close();
             pdfFileBase64 = encodeFileToBase64Binary(path);

         }  catch (FileNotFoundException e) {
             e.printStackTrace();
         } catch (DocumentException e) {
             e.printStackTrace();
         } catch (IOException e) {
             e.printStackTrace();
         }
        return Response.ok(pdfFileBase64, MediaType.TEXT_PLAIN)
                .build();
        //return Response.status(Response.Status.OK).build();
    }

    @GET
    @Path("generateIPADocuments")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response generateIPADocuments(String inputJSON) {

        String pdfFileBase64 = "";
        try

        {


            final File tempFile = File.createTempFile("IPACertificate", ".pdf");
            tempFile.deleteOnExit();

            String path = tempFile.getPath();

            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
            Roles roleObj = gson.fromJson(inputJSON.toString(), Roles.class);
            Roles.CP cpDetails = roleObj.cp;
            Roles.Investor invDetails = roleObj.investor;
            Roles.IPA ipaDetails = roleObj.ipa;
            Roles.NSDL nsdlDetails = roleObj.nsdl;


            Font mainFont = new Font(Font.FontFamily.TIMES_ROMAN, 18.0F, 1);
            new Font(Font.FontFamily.TIMES_ROMAN, 16.0F, 1);
            Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12.0F, 1);
            Font normal = new Font(Font.FontFamily.TIMES_ROMAN, 12.0F);
            Font smallFont = new Font(Font.FontFamily.TIMES_ROMAN, 10.0F);
            SimpleDateFormat dtf = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
            //String allotmentDateStr = dtf.format(cpDetails.allotDate);
            String allotmentDateStr = cpDetails.allotDate;
            DateFormat dtf1 = new SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH);
            String todayStr = dtf1.format(new Date());

            String tempMatValue = cpDetails.maturityValue.replaceAll("[^0-9]", "");
            String tempFaceValue = cpDetails.faceValue.replaceAll("[^0-9]", "");

            Integer units = 0;

            if (tempFaceValue.length() != 0 || tempMatValue.length() != 0) {

                units = Integer.parseInt(tempMatValue) / Integer.parseInt(tempFaceValue);
            }


            Document document = new Document(PageSize.A4, 50.0F, 50.0F, 50.0F, 50.0F);
            PdfWriter.getInstance(document, new FileOutputStream(path));
            document.open();

            Paragraph heading = new Paragraph("Annexure - II\nIPA Certificate - Allotment of CP's", mainFont);
            heading.setAlignment(Element.ALIGN_CENTER);
            document.add(heading);

            document.add(new Paragraph("\n\n"));

            Paragraph ipaName = new Paragraph(ipaDetails.ipaName, smallBold);
            ipaName.setAlignment(Element.ALIGN_RIGHT);
            document.add(ipaName);


            Paragraph ipaAddress = new Paragraph(String.format(ipaDetails.ipaAddress.replaceAll(", ", "\n")), smallFont);
            ipaAddress.setIndentationLeft(400);

            document.add(ipaAddress);


            document.add(new Paragraph(todayStr, normal));

            document.add(new Paragraph("\n"));


            document.add(new Paragraph("To,\n" + nsdlDetails.nsdlContactPerson + "\n" + String.format(nsdlDetails.nsdlAddress.replaceAll(", ", "\n") + "\n\n"), smallFont));

            document.add(new Paragraph("Dear Sir,\n\nWe hereby certify " + cpDetails.issuerName.toUpperCase() +
                    ", the issuers have appointed us as the Issuing and Paying Agent(IPA) for the CP under reference.\n\n", normal));

            document.add(new Paragraph("We confirm that :\n" +
                    "1. The Board resolution of the Issuer authorising the CP has been verified by the Original." +
                    "\n2. The original of duly stamped Commercial Paper has been retained at our end" +
                    "\n3. The issuer has compiled with the RBI guidelines with reference to the issue of CP.\n\n", normal));


            document.add(new Paragraph("The Credits of the CP should be effected to the following account.\n\n" +
                    "1. DP Name" + " : " + ipaDetails.ipaDPName +
                    "\n2. DP ID" + " : " + ipaDetails.ipaAllotmentDPID +
                    "\n3. Client ID" + " : " + ipaDetails.ipaAllotmentClientID +
                    "\n4. Client Name" + " : " + ipaDetails.ipaDPName + " CP Allotment A/c" +
                    "\n5. Quantity" + " : " + units.toString() +
                    "\n6. Date of Credit" + " : " + cpDetails.valueDate +
                    "\n7. ISIN No." + " : " + cpDetails.isinCode + "\n\n", smallBold));


            document.add(new Paragraph("Thanking you,\nSincerely Yours,\n", normal));
            document.add(new Paragraph(ipaDetails.ipaName, smallBold));
            document.add(new Paragraph("Name of the Authorized Signatory", normal));
            document.close();
            pdfFileBase64 = encodeFileToBase64Binary(path);

        }  catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Response.ok(pdfFileBase64, MediaType.TEXT_PLAIN)
                .build();
    }

    @GET
    @Path("generateDealConfirmationDocument")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response generateDealConfirmationDocument(String inputJSON) {

        String pdfFileBase64 = "";
        try {
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
            Roles roleObj = gson.fromJson(inputJSON.toString(), Roles.class);
            Roles.CP cpDetails = roleObj.cp;

            Roles.Investor invDetails = roleObj.investor;
            Roles.IPA ipaDetails = roleObj.ipa;
            Roles.NSDL nsdlDetails = roleObj.nsdl;

            final File tempFile = File.createTempFile("IPA", ".pdf");

            tempFile.deleteOnExit();

            String path = tempFile.getPath();

            Font mainFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
                    Font.BOLD);
            Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
                    Font.BOLD);
            Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
                    Font.BOLD);
            Font normal = new Font(Font.FontFamily.TIMES_ROMAN, 12);

            Font smallFont = new Font(Font.FontFamily.TIMES_ROMAN, 10);


            DateFormat dtf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
            DateFormat dtf1 = new SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH);
            String todayStr = dtf1.format(new Date());
            //String today = dtf1.format(new Date());

               /* String dueDateStr = dtf.format(cpDetails.matDate);
                String validityStr = dtf.format(cpDetails.ratingValidityDate);
                String dateOfRatingStr = dtf.format(cpDetails.dateOfRating);
                String effectiveDateForRatingStr = dtf.format(cpDetails.effectiveDateOfRating);
                String valueDateStr = dtf.format(cpDetails.valueDate);
                String dateOfContractStr = dtf.format(cpDetails.dateOfContract);
                String tradeDateStr = dtf.format(cpDetails.tradeDate);*/

            String dueDateStr = cpDetails.matDate;
            String validityStr = cpDetails.ratingValidityDate;
            String dateOfRatingStr = cpDetails.dateOfRating;
            String effectiveDateForRatingStr = cpDetails.effectiveDateOfRating;
            String valueDateStr = cpDetails.valueDate;
            String dateOfContractStr = cpDetails.dateOfContract;
            String tradeDateStr = cpDetails.tradeDate;
            // Instantiation of document object
            Document document = new Document(PageSize.A4, 50, 50, 50, 50);

            // Creation of PdfWriter object
            PdfWriter writer = PdfWriter.getInstance(document,
                    new FileOutputStream(path));

            document.open();

            // Creation of paragraph object
            Paragraph heading = new Paragraph("DEAL CONFIRMATION NOTE / CONTRACT NOTE", mainFont);
            heading.setAlignment(Element.ALIGN_CENTER);
            document.add(heading);
            document.add(new Paragraph("\n"));

            document.add(new Paragraph("Date of contract : " + dateOfContractStr, normal));
            document.add(new Paragraph("CP (Maturity Value) : " + cpDetails.maturityValue, normal));
            document.add(new Paragraph("Due Date : " + dueDateStr, normal));
            document.add(new Paragraph("Price : " + cpDetails.price, normal));
            document.add(new Paragraph("Disc. rate : " + cpDetails.discRate, normal));
            document.add(new Paragraph("Issue Reference : " + cpDetails.issueRef, normal));
            document.add(new Paragraph("ISIN Code : " + cpDetails.isinCode, normal));
            document.add(new Paragraph("Credit Rating : " + cpDetails.creditRating, normal));
            document.add(new Paragraph("Issued by : " + cpDetails.ratingIssuedBy, normal));
            document.add(new Paragraph("Date of rating : " + dateOfRatingStr, normal));
            document.add(new Paragraph("Validity : " + validityStr, normal));
            document.add(new Paragraph("Effective date for rating : " + effectiveDateForRatingStr, normal));
            document.add(new Paragraph("For amount : " + cpDetails.issueValue, normal));
            document.add(new Paragraph("Conditions (if any) : " + cpDetails.conditions, normal));
            document.add(new Paragraph("Credit Support (if any) : " + cpDetails.creditSupport, normal));
            document.add(new Paragraph("Description of instrument : " + cpDetails.desc, normal));
            document.add(new Paragraph("Amount : " + cpDetails.totalAmount, normal));
            document.add(new Paragraph("Issued by : " + cpDetails.issuerName, normal));
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("In favor of : " + invDetails.investorName, smallBold));
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("Issuing and Paying Agent : " + ipaDetails.ipaName, normal));
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("Issuer's liability under the CP will continue beyond the due date, in case the CP " +
                    "is not paid on due date, even if the CP in D-MAT form is extinguished on due date", normal));
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("Seller of CP : " + cpDetails.issuerName, normal));
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("Purchaser of CP : " + invDetails.investorName, smallBold));
            document.add(new Paragraph("\n\n"));


            document.add(new Paragraph("SETTLEMENT INSTRUCTIONS", subFont));
            document.add(new Paragraph("Value Date : " + valueDateStr, normal));
            document.add(new Paragraph("Please credit to : " + cpDetails.creditTo + " " + cpDetails.currentAccNo + " " + cpDetails.IFSCCode, normal));
            document.add(new Paragraph("Please deliver to : " + invDetails.investorName, smallBold));
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("DP Name : " + invDetails.dpName, normal));
            document.add(new Paragraph("Client ID : " + invDetails.clientId, normal));
            document.add(new Paragraph("DP ID : " + invDetails.dpId, normal));
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("Market conventions : " + cpDetails.marketConventions, normal));
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("The deal is done by : ", smallBold));
            document.add(new Paragraph("(ON BEHALF OF SELLER) : " + cpDetails.issuerContactPerson, normal));
            document.add(new Paragraph("(ON BEHALF OF PURCHASER) : " + invDetails.contactPerson, normal));
            document.add(new Paragraph("(ON TRADE DATE) : " + tradeDateStr, normal));
            document.add(new Paragraph("No recourse is available to the purchaser of CP against previous holders of the CP. : ", normal));
            document.add(new Paragraph("This contract note is executed by \n", normal));
            document.add(new Paragraph("ON BEHALF OF : ", smallBold));
            document.add(new Paragraph("\n\n"));

            document.add(new Paragraph(cpDetails.issuerName, smallBold));
            document.add(new Paragraph("\n\n"));
            document.add(new Paragraph("ON BEHALF OF \n\n", smallBold));
            document.add(new Paragraph(invDetails.investorName, smallBold));


            document.newPage();
            document.add(new Paragraph(todayStr, normal));
            document.add(new Paragraph("\n"));
            document.add(new Paragraph(ipaDetails.ipaName, smallBold));
            document.add(new Paragraph(ipaDetails.ipaAddress, normal));
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("Sub: Issuance of Commercial Paper in Dematerialised form.", smallBold));
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("We refer to the Issuing and Paying Agency Agreement executed with you in" +
                    " connection with the issue of Commercial Paper in dematerialised form as per the guidelines issued" +
                    " by Reserve Bank of India\nIn connection with the above, we would like to confirm that:\n"));
            document.add(new Paragraph("a) We are eligible to issue commercial Paper as per the norms fixed by " +
                    "Reserve Bank of India.\n"));
            document.add(new Paragraph("b) The amount of " + cpDetails.maturityValue + " proposed to be raised by us by " +
                    "issue of Commercial Paper including the amounts already raised is within the amounts mentioned" +
                    "by " + cpDetails.ratingIssuedBy + " vide their letter dated " + dateOfRatingStr + " and amounts" +
                    " approved by the Board.\n"));
            document.add(new Paragraph("c) In terms of (i) Net worth (ii) working capital facilities sanctioned by" +
                    " banks/financial institutions\n\n"));
            document.add(new Paragraph("For " + cpDetails.issuerName + "\n\n", smallBold));
            document.add(new Paragraph("Authorised Signatories\n\n", smallBold));


            document.newPage();
            Paragraph title = new Paragraph("ANNEXURE II", subFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph("\n\n"));
            document.add(new Paragraph(todayStr, normal));
            document.add(new Paragraph("To\nThe Chief General Manager\nFinancial Markets Department\n" +
                    "Reserve Bank of India (RBI)\nCentral Ofiice\nMumbai – 400 001\n\n", normal));
            document.add(new Paragraph("Through \n", normal));
            document.add(new Paragraph(ipaDetails.ipaName, smallBold));
            document.add(new Paragraph(ipaDetails.ipaAddress + "\n", normal));
            document.add(new Paragraph("Dear Sir, \n", normal));
            document.add(new Paragraph("Issue of Commercial Paper", smallBold));
            document.add(new Paragraph("In terms of the Guidelines for issuance of Commercial paper issued " +
                    "by the Reserve Bank of India (RBI) dated August 19,2003 we have issued Commercial Paper as per" +
                    " details furnished hereunder : \n", normal));
            document.add(new Paragraph("i)  Name of issuer :  " + cpDetails.issuerName, normal));
            document.add(new Paragraph("ii) Registered Office and Address :  " + cpDetails.issuerAddress, normal));
            document.add(new Paragraph("iii) Business Activity : " + cpDetails.businessActivity, normal));
            document.add(new Paragraph("iv) Name/s of Stock Exchange/s with whom shares of the issuer \n" +
                    "are listed (if applicable)\n : " + cpDetails.stockExchange, normal));
            document.add(new Paragraph("v) Tangible net worth as per latest audited balance sheet : \n" +
                    cpDetails.netWorth, smallBold));
            document.add(new Paragraph("vi) Total Working Capital Limit : " + cpDetails.workingCapitalLimit, smallBold));
            document.add(new Paragraph("vii) Outstanding  Bank Borrowings : " + cpDetails.outstandingBankBorrowing, normal));
            document.add(new Paragraph("viii) (a) Details of Commercial Paper ", normal));
            document.add(new Paragraph("Issued (Face Value) : " + cpDetails.faceValue, normal));
            document.add(new Paragraph("Date of Issue : " + cpDetails.valueDate, normal));
            document.add(new Paragraph("Date of Maturity : " + cpDetails.matDate, normal));
            document.add(new Paragraph("Amount : " + cpDetails.maturityValue, normal));
            document.add(new Paragraph("Rate : " + cpDetails.discRate, normal));
            document.add(new Paragraph("b) Amount of CP outstanding (Face value) Including the present issue (Including" +
                    " Short term Debts) : " + cpDetails.amountOfCPOutstanding, normal));
            document.add(new Paragraph("ix) Rating(s) obtained from the Credit Rating Information Services of India " +
                    "Ltd. (CRISIL) Or any other agency approved by the Reserve Bank of India (RBI) : "
                    + cpDetails.creditRating, normal));
            document.add(new Paragraph("For and on behalf of" + cpDetails.effectiveDateOfRating, normal));
            document.add(new Paragraph("\n\n", normal));
            document.add(new Paragraph(cpDetails.issuerName, normal));


            document.newPage();
            Paragraph title1 = new Paragraph("Annexure- VI", subFont);
            title1.setAlignment(Element.ALIGN_CENTER);
            document.add(title1);
            Paragraph subTitle = new Paragraph("ISSUE OF COMMERCIAL PAPER (C P )", subFont);
            subTitle.setAlignment(Element.ALIGN_CENTER);
            document.add(subTitle);
            Paragraph subTitle1 = new Paragraph("LETTER OF OFFER", subFont);
            subTitle1.setAlignment(Element.ALIGN_CENTER);
            document.add(subTitle1);

            document.add(new Paragraph("PART I", smallBold));
            document.add(new Paragraph("PROPOSED DATE OF ISSUE : " + cpDetails.valueDate, normal));
            // document.add(new Paragraph("TENOR (in days) : "
            //         + dateDiff(cpDetails.valueDate.toString(), cpDetails.maturityDate.toString()), normal));
            document.add(new Paragraph("DUE DATE : " + cpDetails.matDate, normal));
            document.add(new Paragraph("ISSUE REFERENCE : " + cpDetails.issueRef, normal));
            document.add(new Paragraph("ISIN CODE : " + cpDetails.isinCode, normal));
            document.add(new Paragraph("ISSUE SIZE (Maturity Value) : " + cpDetails.maturityValue, normal));
            document.add(new Paragraph("CREDIT RATING : " + cpDetails.creditRating, normal));
            document.add(new Paragraph("ISSUED BY : " + cpDetails.ratingIssuedBy, normal));
            document.add(new Paragraph("DATE OF RATING : " + cpDetails.effectiveDateOfRating, normal));
            document.add(new Paragraph("VALIDITY : " + cpDetails.ratingValidityDate, normal));
            document.add(new Paragraph("FOR AMOUNT : " + cpDetails.issueValue, normal));
            document.add(new Paragraph("CONDITIONS (if any) : " + cpDetails.conditions, normal));
            document.add(new Paragraph("CREDIT SUPPORT  (if any) : " + cpDetails.creditSupport, normal));
            document.add(new Paragraph("DESCRIPTION OF INSTRUMENT : " + cpDetails.desc, normal));
            document.add(new Paragraph("AMOUNT : " + cpDetails.totalAmount, normal));
            document.add(new Paragraph("ISSUED BY : " + cpDetails.issuerName, normal));
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("IN FAVOUR OF : " + invDetails.investorName, smallBold));
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("ISSUER OF CP / HOLDERS OF CP : " + cpDetails.issuerName, normal));
            document.add(new Paragraph("Issuing and Paying Agent : " + ipaDetails.ipaName, normal));
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("MARKET CONVENTIONS : " + cpDetails.marketConventions, normal));
            document.add(new Paragraph("SUPPORTING BOARD RESOLUTION : " + cpDetails.boardResolutionDate, normal));
            document.add(new Paragraph("TOTAL CP OUTSTANDING (as on date) (Including Short term Debts)" +
                    " : " + cpDetails.amountOfCPOutstanding, normal));


            pdfFileBase64 = encodeFileToBase64Binary(path);

        }  catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Response.ok(pdfFileBase64, MediaType.TEXT_PLAIN)
                .build();



    }

    @GET
    @Path("generateCAFDocument")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response generateCAFDocument(String inputJSON) {

        String pdfFileBase64 = "";
        try {



            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
            Roles roleObj = gson.fromJson(inputJSON.toString(), Roles.class);
            Roles.CP cpDetails = roleObj.cp;
            Roles.Investor invDetails = roleObj.investor;
            Roles.IPA ipaDetails = roleObj.ipa;
            Roles.NSDL nsdlDetails = roleObj.nsdl;

            final File tempFile = File.createTempFile("CAF", ".pdf");
            tempFile.deleteOnExit();

            String path = tempFile.getPath();

            String tempMatValue = cpDetails.maturityValue.replaceAll("[^0-9]", "");
            String tempFaceValue = cpDetails.faceValue.replaceAll("[^0-9]", "");

            Integer units = 0;

            if (tempFaceValue.length() != 0 || tempMatValue.length() != 0) {

                units = Integer.parseInt(tempMatValue) / Integer.parseInt(tempFaceValue);
            }
            Font mainFont = new Font(Font.FontFamily.TIMES_ROMAN, 18.0F, 1);
            new Font(Font.FontFamily.TIMES_ROMAN, 16.0F, 1);
            Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12.0F, 1);
            Font normal = new Font(Font.FontFamily.TIMES_ROMAN, 12.0F);
            Font smallFont = new Font(Font.FontFamily.TIMES_ROMAN, 10.0F);
            SimpleDateFormat dtf = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
            //String allotmentDateStr = dtf.format(cpDetails.allotDate);
            String allotmentDateStr = cpDetails.allotDate;
            DateFormat dtf1 = new SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH);
            String todayStr = dtf1.format(new Date());


            Document document = new Document(PageSize.A4, 50.0F, 50.0F, 50.0F, 50.0F);
            PdfWriter.getInstance(document, new FileOutputStream(path));
            document.open();
            Paragraph issuerPara = new Paragraph(cpDetails.issuerName + "\n"
                    + String.format(cpDetails.issuerAddress.replaceAll(", ", "\n")) + "\nT: " + cpDetails.officialContactNum
                    + "\nF: " + cpDetails.officialFaxNum + "\n" + cpDetails.officialEmail + "\n" +
                    cpDetails.officialWebsite + "\nDate: " + todayStr + "\nCIN: " + cpDetails.CIN, smallFont);
            issuerPara.setIndentationLeft(300);
            document.add(issuerPara);
            document.add(new Paragraph("To,\n" + nsdlDetails.nsdlContactPerson + "\n" + String.format(nsdlDetails.nsdlAddress.replaceAll(", ", "\n") + "\n"), normal));
            document.add(new Paragraph("We wish to issue Commercial Papers (CP) of our company in the demat mode." +
                    " The details of the CP are as follows: \n", normal));


            PdfPTable t = new PdfPTable(2);


            t.addCell(new Phrase("ISIN", smallFont));
            t.addCell(new Phrase(cpDetails.isinCode, smallBold));
            t.addCell(new Phrase("Date of Allotment ", smallFont));
            t.addCell(new Phrase(allotmentDateStr, smallFont));
            t.addCell(new Phrase("Face Value", smallFont));
            t.addCell(new Phrase(cpDetails.faceValue, smallFont));
            t.addCell(new Phrase("No. of Units to be credited", smallFont));
            t.addCell(new Phrase(units.toString(), smallFont));
            t.addCell(new Phrase("Name of the IPA", smallFont));
            t.addCell(new Phrase(ipaDetails.ipaName, smallFont));
            t.addCell(new Phrase("IPA CP Allotment Account - DP ID", smallFont));
            t.addCell(new Phrase(ipaDetails.ipaAllotmentDPID, smallFont));
            t.addCell(new Phrase("IPA CP Allotment Account - Client ID", smallFont));
            t.addCell(new Phrase(ipaDetails.ipaAllotmentClientID, smallFont));
            t.addCell(new Phrase("IPA CP Redemption Account - DP ID", smallFont));
            t.addCell(new Phrase(ipaDetails.ipaRedemptionDPID, smallFont));
            t.addCell(new Phrase("IPA CP Redemption Account - Client ID", smallFont));
            t.addCell(new Phrase(ipaDetails.ipaRedemptionClientID, smallFont));

            document.add(t);
            document.add(new Paragraph("We request you to credit the above mentioned securities to the CP Allotment" +
                    " account of the IPA.", normal));
            document.add(new Paragraph("Yours faithfully,", normal));
            document.add(new Paragraph("Name of the Authorized Signatory", normal));
            document.close();
            pdfFileBase64 = encodeFileToBase64Binary(path);

        }  catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Response.ok(pdfFileBase64, MediaType.TEXT_PLAIN)
                .build();
    }



    @GET
    @Path("getDocs/{docHash}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getDocs(@PathParam("docHash") String docHash)
    {
        return DLAttachmentUtils.getInstance().downloadAttachment(docHash);
    }

    @GET
    @Path("getDoc/{docHash}/{docSubType}/{docExtension}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getDoc(@PathParam("docHash") String docHash,
                           @PathParam("docSubType") String docSubType,
                           @PathParam("docExtension") String docExtension)
    {
        return DLAttachmentUtils.getInstance().downloadAttachment(docHash + "/" + docSubType + "." + docExtension);
    }

    @GET
    @Path("getDoc")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getDoc(IndiaCPDocumentDetails documentDetails)
    {
        return DLAttachmentUtils.getInstance().downloadAttachment(documentDetails.getDocHash() + "/" + documentDetails.getDocSubType() + "." + documentDetails.getDocExtension());
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
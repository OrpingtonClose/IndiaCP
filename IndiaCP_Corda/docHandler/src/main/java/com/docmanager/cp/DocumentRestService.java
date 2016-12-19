package com.docmanager.cp;

/**
 * Created by Electrania.com on 12/10/2016.
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.logging.Log;
import org.json.JSONObject;
import org.json.JSONException;


/**
 * Created by Shweta Chawla on 12/1/2016.
 */
//http://localhost:8080/DocumentHandler/rest/document/depositoryLetter/
@Path("document")
public class DocumentRestService{

    private static final Logger logger = Logger.getLogger(DocumentRestService.class.getName() );

    @GET
    @Path("/depositoryLetter/{cpJsonObj}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response generateDepositoryLetter(
            JSONObject cpJsonObj
    ) throws DocumentException,
            IOException {

        Gson gson = new Gson();
        //CP cpDetails = (CP) cpJsonObj.get("cpDetails");
        Roles.CP cpDetails = gson.fromJson(cpJsonObj.toString(), Roles.CP.class);


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

        //FOR TESTING PURPOSE
        /*
        issuerName = "Barclays Investment and Loans (India) Ltd.";
        secondParty = "Link Intime Link Intime India Pvt. Ltd.";

        issueValue = "Rs. 736,629,750/-";
        redemptionValue = "Rs. 750,000,000";
        tax = "Rs. 10000/-";
        CIN = "U93090TN1937FLC001429";
        NSDLHistory = "Yes";
        issuerAddress = "Regus Chennai Citi Centre Level 6, 10/11, Dr. Radhakrishnan Salai Mylapore, Chennai," +
                " Tamilnadu-600004";
        entityType = "Joint Stock Company";
        */

        // Instantiation of document object
        Document document = new Document(PageSize.A4, 50, 50, 50, 50);

        // Creation of PdfWriter object
        PdfWriter writer = PdfWriter.getInstance(document,
                new FileOutputStream("E:\\DepositoryLetter.pdf"));

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

        document.add(new Paragraph("National Securities Depository Limited", normal));
        document.add(new Paragraph("4th Floor, Trade World", normal));
        document.add(new Paragraph("Kamala Mills Compound", normal));
        document.add(new Paragraph("Senapati Bapat Marg", normal));
        document.add(new Paragraph("Lower Parel", normal));
        document.add(new Paragraph("4th Floor, Trade World", normal));
        document.add(new Paragraph("Mumbai - 400 013.", normal));

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
        /*switch (entityType)
        {
            ("Joint Stock Company") : "01 : Joint Stock Company";


        }*/
        document.add(new Paragraph("Type of entity :" + cpDetails.entityType, smallBold));
        document.close();
        File pdfFile = new File("E:\\DepositoryLetter.pdf");

        return Response.ok(pdfFile, MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-Disposition", "attachment; filename=\"" + pdfFile.getName() + "\"" ) //optional
                .build();


    }

    @GET
    @Path("/dealConfirmationDoc/{dealDetails}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response generateDealConf(JSONObject dealDetails) throws DocumentException,
            IOException, Exception {

        Gson gson = new Gson();
        Roles roleObj = gson.fromJson(dealDetails.toString(), Roles.class);
        Roles.CP cpDetails = null;
        Roles.Investor invDetails = null;
        Roles.IPA ipaDetails = null;

        for (Roles.Container container : roleObj)
        {
            String innerDealInfo = gson.toJson(container.object);
            switch (container.type)
            {
                case 1 :
                    cpDetails = gson.fromJson(innerDealInfo, Roles.CP.class);
                    break;
                case 2 :
                    invDetails = gson.fromJson(innerDealInfo, Roles.Investor.class);
                    break;
                case 3 :
                    ipaDetails = gson.fromJson(innerDealInfo, Roles.IPA.class);
                    break;
                default:
                    break;
            }
        }

        if(cpDetails != null || invDetails != null || ipaDetails != null) {

            Font mainFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
                    Font.BOLD);
            Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
                    Font.BOLD);
            Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
                    Font.BOLD);
            Font normal = new Font(Font.FontFamily.TIMES_ROMAN, 12);

            Font smallFont = new Font(Font.FontFamily.TIMES_ROMAN, 10);

            /* FOR Testing Purpose
            String issuerName = "Barclays Investments & Loans (India) Ltd";
            String investorName = "Barclays Shared Services Pvt Ltd";
            String maturityValue = "Rs. 250,000,000/-";
            Double price = 96.9018;
            String discRate = "7.78%";
            String issueRef = "CP/2015/95";
            String ISINCode = "INE704I14676";
            String creditRating = "ICRA A1";
            String issuedBy = "ICRA";
            String forAmount = "Rs. 1500 Crores";
            String conditions = "N.A";
            String creditSupport = "N.A";
            String descOfInstrument = "Commercial Paper 95";
            String amount = "Rs. 250,000,000/-";
            String issuingAndPayingAgent = "HDFC Bank Ltd";
            String creditInstructions = "Barclays Investment & Loans (India) Ltd CP \n Current A/C number : 00600350084976" +
                    "with HDFC Bank Ltd, Through RTGS IFSC Code HDFC0000060 for Rs. 242,254,500/-";
            String DPname = "Barclays Securities India Pvt Ltd";
            String ClientId = "10021709";
            String DPId = "IN303559";
            String marketConventions = "FIMMDA Conventions";
            String sellerContactPerson = "Mr. Jong-Jin Park";
            String purchaserContactPerson = "over Phone";
            */
            DateFormat dtf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
            DateFormat dtf1 = new SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH);
            String todayStr = dtf1.format(new Date());
            String today = dtf1.format(new Date());

            String dueDateStr = dtf.format(cpDetails.maturityDate);
            String validityStr = dtf.format(cpDetails.ratingValidityDate);
            String dateOfRatingStr = dtf.format(cpDetails.dateOfRating);
            String effectiveDateForRatingStr = dtf.format(cpDetails.effectiveDateOfRating);
            String valueDateStr = dtf.format(cpDetails.valueDate);
            String dateOfContractStr = dtf.format(cpDetails.dateOfContract);
            String tradeDateStr = dtf.format(cpDetails.tradeDate);
            // Instantiation of document object
            Document document = new Document(PageSize.A4, 50, 50, 50, 50);

            // Creation of PdfWriter object
            PdfWriter writer = PdfWriter.getInstance(document,
                    new FileOutputStream("E:\\ConfirmationLetter.pdf"));

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
            document.add(new Paragraph("Description of instrument : " + cpDetails.description, normal));
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
            document.add(new Paragraph("Please credit to : " + cpDetails.creditInstructions, normal));
            document.add(new Paragraph("Please deliver to : " + invDetails.investorName, smallBold));
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("DP Name : " + invDetails.dpName, normal));
            document.add(new Paragraph("Client ID : " + invDetails.clientId, normal));
            document.add(new Paragraph("DP ID : " + invDetails.dpId, normal));
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("Market conventions : " + cpDetails.marketConventions, normal));
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("The deal is done by : ", smallBold));
            document.add(new Paragraph("(ON BEHALF OF SELLER) : " + cpDetails.contactPerson, normal));
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
            document.add(new Paragraph("Date of Maturity : " + cpDetails.maturityDate, normal));
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
            document.add(new Paragraph("TENOR (in days) : "
                    + dateDiff(cpDetails.valueDate.toString(), cpDetails.maturityDate.toString()), normal));
            document.add(new Paragraph("DUE DATE : " + cpDetails.maturityDate, normal));
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
            document.add(new Paragraph("DESCRIPTION OF INSTRUMENT : " + cpDetails.description, normal));
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
        }
        else
        {
            //Display error of absence of details in json
        }

        File pdfFile = new File("E:\\ConfirmationLetter.pdf");

        return Response.ok(pdfFile, MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-Disposition", "attachment; filename=\"" + pdfFile.getName() + "\"" ) //optional
                .build();
    }

    @GET
    @Path("hash/{inFile}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.TEXT_PLAIN)
    public String getHashOfDoc(File inFile) throws HashGenerationException {
        try {
            FileInputStream inputStream = new FileInputStream(inFile);
            byte[] bytesBuffer = new byte[1024];
            int bytesRead = -1;

            String algorithm = "SHA-256";

            MessageDigest digest = MessageDigest.getInstance(algorithm);

            // generate in parts in case of a large sized file
            while ((bytesRead = inputStream.read(bytesBuffer)) != -1) {
                digest.update(bytesBuffer, 0, bytesRead);
            }

            byte[] hashedBytes = digest.digest();

            return convertByteArrayToHexString(hashedBytes);

        } catch (Exception ex) {
            throw new HashGenerationException(
                    "Could not generate hash from File", ex);
        }
    }

    ///To convert a Byte array to Hex String
    ///
    ///
    private static String convertByteArrayToHexString(byte[] arrayBytes) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < arrayBytes.length; i++) {
            stringBuffer.append(Integer.toString((arrayBytes[i] & 0xff) + 0x100, 16)
                    .substring(1));
        }
        return stringBuffer.toString();
    }

    /// To get number of days between two dates
    ///
    ///
    private static String dateDiff(String startDate, String endDate) throws Exception
    {
        String dayDifference = "";
        try {

            Date date1;
            Date date2;

            SimpleDateFormat dates = new SimpleDateFormat("MM/dd/yyyy");

            //Setting dates
            date1 = dates.parse(startDate);
            date2 = dates.parse(endDate);

            //Comparing dates
            long difference = Math.abs(date1.getTime() - date2.getTime());
            long differenceDates = difference / (24 * 60 * 60 * 1000);

            //Convert long to String
            dayDifference = Long.toString(differenceDates);



        } catch (Exception exception) {

            logger.log(Level.FINE,"Date difference could not be calculated" + exception );
        }
        return dayDifference;
    }

}

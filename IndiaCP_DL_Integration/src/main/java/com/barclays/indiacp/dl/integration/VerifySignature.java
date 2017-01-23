package com.barclays.indiacp.dl.integration;


import com.barclays.indiacp.dl.EMVerifyClient.DSVerifyWSImplServiceLocator;
import com.barclays.indiacp.dl.EMVerifyClient.DSVerifyWSImplServiceSoapBindingStub;
import com.barclays.indiacp.model.VerificationResult;
import org.apache.axis.encoding.Base64;
import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;



/**
 * Created by Electrania.com on 1/14/2017.
 */
public class VerifySignature {

    public String verify(String base64encoded)
    {
        String status = "";

        try{

            DSVerifyWSImplServiceSoapBindingStub binding = (DSVerifyWSImplServiceSoapBindingStub)new DSVerifyWSImplServiceLocator().getDSVerifyWSImplPort();

            status = binding.verify("pdf", base64encoded ,"xml");

        } catch (javax.xml.rpc.ServiceException ex) {
            ex.printStackTrace();
        } catch (java.rmi.RemoteException ex) {
            ex.printStackTrace();
        }

        return status;
    }

    public String convertIStoB64(InputStream is)
    {
        String str = "";
        try {
        byte[] bytes = IOUtils.toByteArray(is);
        str = Base64.encode(bytes);
        } catch (IOException e)
        {e.printStackTrace();}
        return str;
    }

    public VerificationResult readXML(String xmlResult) {
        VerificationResult result = new VerificationResult();



       try {
           DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
           InputSource is = new InputSource();
           is.setCharacterStream(new StringReader(xmlResult));

           Document doc = db.parse(is);

           NodeList nList = doc.getElementsByTagName(doc.getDocumentElement().getNodeName());



               Node nNode = nList.item(0);

               //System.out.println("\nCurrent Element :" + nNode.getNodeName());

               if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                   Element eElement = (Element) nNode;

                   //System.out.println("Staff id : " + eElement.getAttribute("id"));
                   result.result = eElement.getElementsByTagName("status").item(0).getTextContent();
                   result.issuerOrganisation = eElement.getElementsByTagName("issuerOrganisation").item(0).getTextContent();
                   result.serialNumber = eElement.getElementsByTagName("serialNumber").item(0).getTextContent();
                   result.signerName = eElement.getElementsByTagName("commonName").item(0).getTextContent();
                   result.organization = eElement.getElementsByTagName("organisation").item(0).getTextContent();
                   result.signStatus = eElement.getElementsByTagName("signatureStatus").item(0).getChildNodes().item(1).getTextContent();
                   result.trustStatus = eElement.getElementsByTagName("trustStatus").item(0).getChildNodes().item(1).getTextContent();
                   result.expiryStatus = eElement.getElementsByTagName("expiryStatus").item(0).getChildNodes().item(1).getTextContent();
               }

       }
       catch (IOException e)
       {
           e.printStackTrace();
       } catch (Exception e) {
           e.printStackTrace();
       }
        return result;
    }

    public Boolean verifyWithOrg(InputStream is, String docType, String organization)
    {
        VerificationResult vr = readXML(verify(convertIStoB64(is)));
        if(vr.organization.equals(organization) && vr.result.equals("success")) {
            return true;
        }else
            return false;
    }

}

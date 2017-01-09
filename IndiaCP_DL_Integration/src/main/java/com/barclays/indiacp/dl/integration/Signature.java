package com.barclays.indiacp.dl.integration;
import com.barclays.indiacp.dl.EmudhraClient.DSVerifyWSImplServiceLocator;
import com.barclays.indiacp.dl.EmudhraClient.DSVerifyWSImplServiceSoapBindingStub;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import org.apache.axis.encoding.Base64;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;


/**
 * Created by Electrania.com on 1/8/2017.
 */
public class Signature {

    public String signPDF(String b64EncodedString)
    {
        String signed = "";

        try{

            DSVerifyWSImplServiceSoapBindingStub binding = (DSVerifyWSImplServiceSoapBindingStub)new DSVerifyWSImplServiceLocator().getDSVerifyWSImplPort();
            signed = binding.signPdf("test1", b64EncodedString,"", "400,170,500,250", "first", "test", "test");
            } catch (javax.xml.rpc.ServiceException ex) {
                ex.printStackTrace();
            } catch (java.rmi.RemoteException ex) {
                ex.printStackTrace();
            }

            return signed;
    }

    public String signFolder(String folderPath, String docType) {

        String destination = System.getProperty("java.io.tmpdir") + "\\" + docType + "\\";
        File dir = new File(folderPath);
        File[] directoryListing = dir.listFiles();
        ArrayList<String> filesPath = null;
        if (directoryListing != null) {
            for (File child : directoryListing) {
                filesPath.add(Base64Decode(signPDF(Base64Encode(child.getPath())), docType));
            }
        }

        return Base64Encode(zipFile(destination,docType));

    }

    public String unzipFile(String b64EncodedString, String docType)
    {
        String destination = System.getProperty("java.io.tmpdir") + "\\" + docType + "\\";

      try {
          byte[] decoded = Base64.decode(b64EncodedString);
          final File tempFile = File.createTempFile("temp", ".zip");
          tempFile.deleteOnExit();
          FileOutputStream output = new FileOutputStream(tempFile);
          output.write(decoded);
          output.close();

          String source = tempFile.getAbsolutePath();

              ZipFile zipFile = new ZipFile(source);
              //if (zipFile.isEncrypted()) {
              //   zipFile.setPassword(password);
              //}
              zipFile.extractAll(destination);



      } catch (ZipException e) {
          e.printStackTrace();
      }
      catch (IOException e)
      {
          e.printStackTrace();
      }
        return destination;
    }
    public String zipFile(String fileFolderPath, String docType)
    {
        String destination =  System.getProperty("java.io.tmpdir") + "\\" + docType + ".zip";
       try{



           ZipParameters parameters = new ZipParameters();
           parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
           parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
          // parameters.setSourceExternalStream(true);


          /* if(password.length()>0){
               parameters.setEncryptFiles(true);
               parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_AES);
               parameters.setAesKeyStrength(Zip4jConstants.AES_STRENGTH_256);
               parameters.setPassword(password);
           }*/

           ZipFile zipFile = new ZipFile(destination);
           //String folderToAdd = "E:\\Temp";
           //zipFile.addFolder(folderToAdd, parameters);

           File targetFile = new File(fileFolderPath);

           if(targetFile.isFile()){
               zipFile.addFile(targetFile, parameters);
           }else if(targetFile.isDirectory()){
               zipFile.addFolder(fileFolderPath, parameters);
           }



       } catch (ZipException e) {
           e.printStackTrace();
       }

        return destination;
    }

    public String Base64Encode(String filePath)
    {
        String str="";

       try {
           byte[] bytes = IOUtils.toByteArray(new FileInputStream(new File(filePath)));
           str = Base64.encode(bytes);
       }
       catch (IOException e)
       {
           e.printStackTrace();
       }
       return str;

    }

    public String Base64Decode(String base64String, String docType)
    {

        String path = "";
        try {
            byte[] decoded = Base64.decode(base64String);
            File tempFile = File.createTempFile(docType, ".pdf");
            path = tempFile.getAbsolutePath();
            FileOutputStream output = new FileOutputStream(tempFile);
            output.write(decoded);
            output.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return path;
    }




}

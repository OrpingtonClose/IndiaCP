package com.barclays.indiacp.dl.integration;
import com.barclays.indiacp.dl.EmudhraClient.DSVerifyWSImplServiceLocator;
import com.barclays.indiacp.dl.EmudhraClient.DSVerifyWSImplServiceSoapBindingStub;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import org.apache.axis.encoding.Base64;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static java.io.File.createTempFile;


/**
 * Created by Electrania.com on 1/8/2017.
 */
public class Signature {

    public String inputStreamSign(InputStream uploadedInputStream, String docType)
    {
        String signedDocFolder = "";
        InputStream isSigned = null;
        String returnStr = "";
        try {

            signedDocFolder = createTempDir(docType).getAbsolutePath();

            final File tempFile = createTempFile(docType, ".pdf", uploadedInputStream);
            returnStr = signPDF(Base64Encode(tempFile.getAbsolutePath()));


        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return returnStr;
    }

    public String signZipFolder(String ZipFilePath, String docType) {
        String finalPath = "";
        final File tempFile;
        String signedDocFolder = "";


        try {
            String upzippedFolderPath = unzipFile(ZipFilePath, docType);
            //tempFile = createTempFile(docType, ".zip");
            //ZipFile zipFile = new ZipFile(tempFile.getAbsoluteFile());
            //finalPath = tempFile.getAbsolutePath();
            signedDocFolder = createTempDir(docType).getAbsolutePath();
            File dir = new File(upzippedFolderPath);
            File[] directoryListing = dir.listFiles();
            ArrayList<String> filesPath = new ArrayList<String>();

            if (directoryListing != null) {
                for (File child : directoryListing) {
                    filesPath.add(Base64Decode(signPDF(Base64Encode(child.getPath())), docType, signedDocFolder).toString());

                }
            }

            finalPath = zipFile(signedDocFolder,docType);

            // return Base64Encode(zipFile(destination,docType));
        } catch (Exception e){
            e.printStackTrace();
        }
        return finalPath;

    }

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



    public String unzipFile(String zipFilePath, String docType)
    {
        String destination = "";

        try{

            destination = createTempDir(docType).getAbsolutePath();
         /* byte[] decoded = Base64.decode(b64EncodedString);
          final File tempFile = File.createTempFile("temp", ".zip");

         tempFile.deleteOnExit();
         FileOutputStream output = new FileOutputStream(tempFile);
         output.write(decoded);
         output.close();

          String source = tempFile.getAbsolutePath();
*/
              ZipFile zipFile = new ZipFile(zipFilePath);
              //if (zipFile.isEncrypted()) {
              //   zipFile.setPassword(password);
              //}
              zipFile.extractAll(destination);

      } catch (ZipException e) {
          e.printStackTrace();
      } catch (Exception e) {
          e.printStackTrace();
        }

        return destination;
    }

    public String zipFile(String fileFolderPath, String docType)
    {



           Calendar calendar = Calendar.getInstance();
           Date time = calendar.getTime();
           long milliseconds = time.getTime();
           String path = System.getProperty("java.io.tmpdir");
           ZipFile zipFile;
           try{
            zipFile = new ZipFile(path + docType + "_" + milliseconds + ".zip");

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
        return (path + docType + "_" + milliseconds + ".zip");

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

    public String Base64Decode(String base64String, String docType, String filePath)
    {

        File dir = new File(filePath);
        String path = "";
        try {
            byte[] decoded = Base64.decode(base64String);
            File tempFile = File.createTempFile(docType, ".pdf", dir);
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

    public static File createTempDir(String prefix)
            throws IOException
    {
        String tmpDirStr = System.getProperty("java.io.tmpdir");
        if (tmpDirStr == null) {
            throw new IOException(
                    "System property 'java.io.tmpdir' does not specify a tmp dir");
        }

        File tmpDir = new File(tmpDirStr);
        if (!tmpDir.exists()) {
            boolean created = tmpDir.mkdirs();
            if (!created) {
                throw new IOException("Unable to create tmp dir " + tmpDir);
            }
        }

        File resultDir = null;
        int suffix = (int)System.currentTimeMillis();
        int failureCount = 0;
        do {
            resultDir = new File(tmpDir, prefix + suffix % 10000);
            suffix++;
            failureCount++;
        }
        while (resultDir.exists() && failureCount < 50);

        if (resultDir.exists()) {
            throw new IOException(failureCount +
                    " attempts to generate a non-existent directory name failed, giving up");
        }
        boolean created = resultDir.mkdir();
        if (!created) {
            throw new IOException("Failed to create tmp directory");
        }

        return resultDir;
    }




    private File createTempFile(String fileName, String extension, InputStream uploadedInputStream) {
        try {
            final File tempFile = File.createTempFile(fileName, extension);
            tempFile.deleteOnExit();
            FileOutputStream out = new FileOutputStream(tempFile);
            IOUtils.copy(uploadedInputStream, out);
            return tempFile;
        } catch (Exception ex)
        {
            throw new RuntimeException("File could not be uploaded.");
        }
    }



}

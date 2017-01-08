package com.barclays.indiacp.dl.utils;

import org.apache.commons.io.IOUtils;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Random;
import java.util.logging.Logger;

/**
 * Created by ritukedia on 05/01/17.
 */
public class DLAttachmentUtils {

    private static DLAttachmentUtils dlAttachmentUtils;

    Logger logger = Logger.getLogger(this.getClass().getName());
    String dlUploadAttachmentPath;
    String dlDownloadAttachmentPath;
    Client jerseyClient;
    WebTarget dlAttachmentRestEndPoint;

    private DLAttachmentUtils() {
        try {
            dlDownloadAttachmentPath = DLConfig.DLConfigInstance().getDLDownloadAttachmentPath() + "/";
            dlUploadAttachmentPath = DLConfig.DLConfigInstance().getDLUploadAttachmentPath();
            jerseyClient = ClientBuilder.newBuilder().register(MultiPartFeature.class).build();
            dlAttachmentRestEndPoint = jerseyClient.target(DLConfig.DLConfigInstance().getDLAttachmentRestEndpoint());
        } catch (Exception e) {
            logger.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static DLAttachmentUtils getInstance() {
        if (dlAttachmentUtils == null) {
            dlAttachmentUtils = new DLAttachmentUtils();
        }
        return dlAttachmentUtils;
    }

    public Response downloadAttachment(String filePath) {
        Response attachmentResponse = dlAttachmentRestEndPoint.path(dlDownloadAttachmentPath + filePath)
                .request(MediaType.APPLICATION_OCTET_STREAM)
                .get();
        return attachmentResponse;
    }

    public String uploadAttachment(InputStream uploadedInputStream) {
        final File tempFile;
        String tempFileName = "IndiaCPDocument_" + new Random().nextInt();

        try {
            tempFile = createTempFile(tempFileName, ".zip", uploadedInputStream);


        } catch (Exception ex)
        {
            throw new RuntimeException("File could not be uploaded.");
        }

        MultiPart multiPart = new MultiPart();
        multiPart.setMediaType(MediaType.MULTIPART_FORM_DATA_TYPE);

        FileDataBodyPart fileDataBodyPart = new FileDataBodyPart("file",
                tempFile,
                MediaType.APPLICATION_OCTET_STREAM_TYPE);
        multiPart.bodyPart(fileDataBodyPart);

        Response attachmentResponse = dlAttachmentRestEndPoint.path(dlUploadAttachmentPath)
                .request(MediaType.MULTIPART_FORM_DATA)
                .post(Entity.entity(multiPart, multiPart.getMediaType()));


        String fileHash = attachmentResponse.readEntity(String.class);
        return fileHash.trim();

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

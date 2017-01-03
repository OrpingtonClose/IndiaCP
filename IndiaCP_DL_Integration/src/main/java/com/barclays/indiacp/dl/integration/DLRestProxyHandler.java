package com.barclays.indiacp.dl.integration;

import com.barclays.indiacp.dl.utils.DLConfig;
import com.barclays.indiacp.model.IndiaCPProgram;
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
import java.util.logging.Logger;

/**
 * Created by ritukedia on 23/12/16.
 */
public class DLRestProxyHandler implements InvocationHandler {
    WebTarget dlRestEndPoint;
    WebTarget dlAttachmentRestEndPoint;
    String resourcePath;
    private Request request;

    public DLRestProxyHandler(String resourcePath) {
        this.resourcePath = resourcePath;
        Logger logger = Logger.getLogger(IndiaCPProgramApi.class.getName());
        Client jerseyClient = ClientBuilder.newBuilder()
                .register(MultiPartFeature.class).build();

        //Client jerseyClient = ClientBuilder.newClient(new ClientConfig().register(org.glassfish.jersey.jsonp.internal.JsonProcessingAutoDiscoverable.class));
        //Feature feature = new LoggingFeature(logger, Level.INFO, null, null);
        //jerseyClient.register(feature);
        dlRestEndPoint = jerseyClient.target(DLConfig.DLConfigInstance().getDLRestEndpoint());
        dlAttachmentRestEndPoint = jerseyClient.target(DLConfig.DLConfigInstance().getDLAttachmentRestEndpoint());
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        if (method.getName().equals("setRequest")) {
            System.out.println("");
        }

        String   methodEndPoint = getMethodEndPoint(method, args);

        String docHash = null;
        if (requiresAttachmentUpload(method)) {
            //upload attachment to DL
            docHash = uploadAttachmentToDL(method.getName(), args);
            applyPathParamToMethodPath(methodEndPoint, "docHash", docHash);
        }

        String jsonString = null;
        if (methodHasJSONBodyParam(method)) {
            jsonString = getJSONToPost(method, args);
        }

        if (method.isAnnotationPresent(javax.ws.rs.GET.class)) {
            Response response = dlRestEndPoint.path(methodEndPoint).request().get();
            return response;
            //return response.readEntity(String.class);
        } else if (method.isAnnotationPresent(javax.ws.rs.POST.class)) {
            if (jsonString != null) {
                Response response = dlRestEndPoint.path(methodEndPoint).request().post(Entity.json(jsonString));
                return response;
                //return response.readEntity(String.class);
            } else {
                Response response = dlRestEndPoint.path(methodEndPoint).request().post(Entity.text(""));
                return response;
                //return response.readEntity(String.class);
            }
        }

        throw new UnsupportedOperationException(method.getAnnotations() + " Not Supported");
    }

    private boolean requiresAttachmentUpload(Method method) {
        Consumes consumesAnnotation = method.getAnnotation(javax.ws.rs.Consumes.class);
        if (consumesAnnotation == null) {
            return false;
        }
        String[] mediaTypes = consumesAnnotation.value();
        for (String mediaType: mediaTypes) {
            if (mediaType.equals(MediaType.MULTIPART_FORM_DATA)) {
                return true;
            }
        }
        return false;
    }


    private String uploadAttachmentToDL(String docName, Object[] args) {

        InputStream uploadedInputStream;
        final File tempFile;

        try {
            uploadedInputStream = (InputStream) args[args.length - 1];

            tempFile = createTempFile(docName, ".zip", uploadedInputStream);


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

           Response attachmentResponse = dlAttachmentRestEndPoint.path(DLConfig.DLConfigInstance().getDLUploadAttachmentPath())
                   .request(MediaType.MULTIPART_FORM_DATA)
                   .post(Entity.entity(multiPart, multiPart.getMediaType()));


           String fileHash = attachmentResponse.readEntity(String.class);
          // System.out.println(attachmentResponse.getStatus() + " " + attachmentResponse.getStatusInfo() + " " + fileHash);



//        StreamDataBodyPart streamDataBodyPart = new StreamDataBodyPart(docName, uploadedInputStream, docName, MediaType.APPLICATION_OCTET_STREAM_TYPE);
//
//        final Response attachmentResponse = dlRestEndPoint.path(DLConfig.DLConfigInstance().getDLUploadAttachmentPath())
//                .request()
//                .post(Entity.entity(streamDataBodyPart, MediaType.MULTIPART_FORM_DATA_TYPE));

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

    private boolean methodHasJSONBodyParam(Method method) {
        if (method.isAnnotationPresent(javax.ws.rs.Consumes.class)) {
            Consumes consumesAnnotation = method.getAnnotation(javax.ws.rs.Consumes.class);
            String[] mediaTypes = consumesAnnotation.value();
            for (String mediaType: mediaTypes) {
                if (mediaType.equals(MediaType.APPLICATION_JSON)) {
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }

    private String getJSONToPost(Method method, Object[] args) {
        //assumes that the last argument is the json body
        return (String)args[args.length - 1];
    }

    private String getMethodEndPoint(Method method, Object[] args)
    {
        Path restMethodPath = null;
        try {
             restMethodPath = method.getAnnotation(javax.ws.rs.Path.class);
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        String path = restMethodPath.value();
        Annotation[][] paramAnnotations = method.getParameterAnnotations();
        int paramIndex = 0;
        for (Annotation[] paramAnnotation: paramAnnotations) {
            for (Annotation param: paramAnnotation) {
                if(param.annotationType().isAssignableFrom(javax.ws.rs.PathParam.class)) {
                    PathParam pp = (PathParam) param;
                    path = applyPathParamToMethodPath(path, pp.value(), args[paramIndex++]);
                }
            }
        }
        return resourcePath + "/" + path;
    }

    private String applyPathParamToMethodPath(String path, String paramName, Object paramValue) {
        return path.replace("{" + paramName + "}", paramValue.toString());
    }

    public void setRequest(Request request) {
        this.request = request;
    }


}

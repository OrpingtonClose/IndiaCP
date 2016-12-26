package com.barclays.indiacp.dl.integration;

import com.barclays.indiacp.dl.utils.DLConfig;
import org.glassfish.jersey.client.ClientResponse;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;
import org.glassfish.jersey.media.multipart.file.StreamDataBodyPart;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.logging.Logger;

import static javax.ws.rs.core.MediaType.APPLICATION_OCTET_STREAM_TYPE;

/**
 * Created by ritukedia on 23/12/16.
 */
public class DLRestProxyHandler implements InvocationHandler {
    WebTarget dlRestEndPoint;
    String resourcePath;

    public DLRestProxyHandler(String resourcePath) {
        this.resourcePath = resourcePath;
        Logger logger = Logger.getLogger(IndiaCPProgram.class.getName());
        Client jerseyClient = ClientBuilder.newClient();
        //Client jerseyClient = ClientBuilder.newClient(new ClientConfig().register(org.glassfish.jersey.jsonp.internal.JsonProcessingAutoDiscoverable.class));
        //Feature feature = new LoggingFeature(logger, Level.INFO, null, null);
        //jerseyClient.register(feature);
        dlRestEndPoint = jerseyClient.target(DLConfig.DLConfigInstance().getDLRestEndpoint());
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        String methodEndPoint = getMethodEndPoint(method, args);

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
           return dlRestEndPoint.path(methodEndPoint).request().get();
        } else if (method.isAnnotationPresent(javax.ws.rs.POST.class)) {
            if (jsonString != null) {
                return dlRestEndPoint.path(methodEndPoint).request().post(Entity.json(jsonString));
            } else {
                return dlRestEndPoint.path(methodEndPoint).request().post(Entity.text(""));
            }
        }

        throw new UnsupportedOperationException(method.getAnnotations() + " Not Supported");
    }

    private boolean requiresAttachmentUpload(Method method) {
        Consumes consumesAnnotation = method.getAnnotation(javax.ws.rs.Consumes.class);
        String[] mediaTypes = consumesAnnotation.value();
        for (String mediaType: mediaTypes) {
            if (mediaType.equals(MediaType.MULTIPART_FORM_DATA)) {
                return true;
            }
        }
        return false;
    }

    private String uploadAttachmentToDL(String docName, Object[] args) {

        InputStream uploadedInputStream = (InputStream) args[args.length - 1];
        StreamDataBodyPart streamDataBodyPart = new StreamDataBodyPart(docName, uploadedInputStream, docName, MediaType.APPLICATION_OCTET_STREAM_TYPE);

        final Response attachmentResponse = dlRestEndPoint.path(DLConfig.DLConfigInstance().getDLUploadAttachmentPath())
                .request()
                .post(Entity.entity(streamDataBodyPart, MediaType.MULTIPART_FORM_DATA_TYPE));

        return attachmentResponse.getEntity().toString(); //TODO: extract hash from attachmentResponse

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

    private String getMethodEndPoint(Method method, Object[] args) {
        Path restMethodPath = method.getAnnotation(javax.ws.rs.Path.class);
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


}

package com.barclays.indiacp.dl.integration;

import com.barclays.indiacp.dl.utils.DLAttachmentUtils;
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

/**
 * Created by ritukedia on 23/12/16.
 */
public class DLRestProxyHandler implements InvocationHandler {
    WebTarget dlRestEndPoint;
    String resourcePath;
    private Request request;

    public DLRestProxyHandler(String resourcePath) {
        this.resourcePath = resourcePath;
        Logger logger = Logger.getLogger(IndiaCPProgramApi.class.getName());
        Client jerseyClient = ClientBuilder.newBuilder()
                .register(MultiPartFeature.class).build();

        dlRestEndPoint = jerseyClient.target(DLConfig.DLConfigInstance().getDLRestEndpoint());
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        if (method.getName().equals("setRequest")) {
            System.out.println("");
        }

        String   methodEndPoint = getMethodEndPoint(method, args);

        String jsonString = null;
        String docHash = null;
        if (requiresAttachmentUpload(method)) {
            //upload attachment to DL
            docHash = DLAttachmentUtils.getInstance().uploadAttachment(method.getName(), args);
            //set docHash in the documentDetails metadata before posting it to the DL
            jsonString = setDocHash(args, docHash);
        }

        if (methodHasJSONBodyParam(method)) {
            jsonString = getJSONToPost(method, args);
        }

        if (method.isAnnotationPresent(javax.ws.rs.GET.class)) {
            Response response = dlRestEndPoint.path(methodEndPoint).request().get();
            return response;
        } else if (method.isAnnotationPresent(javax.ws.rs.POST.class)) {
            if (jsonString != null) {
                Response response = dlRestEndPoint.path(methodEndPoint).request().post(Entity.json(jsonString));
                return response;
            } else {
                Response response = dlRestEndPoint.path(methodEndPoint).request().post(Entity.text(""));
                return response;
            }
        }

        throw new UnsupportedOperationException(method.getAnnotations() + " Not Supported");
    }

    private String setDocHash(Object[] args, String docHash) {
        IndiaCPDocumentDetails docDetails = null;
        for (Object arg : args) {
            if (arg instanceof IndiaCPDocumentDetails) {
                docDetails = (IndiaCPDocumentDetails) arg;
            }
        }
        if (docDetails == null) {
            throw new RuntimeException("Expected IndiaCPDocumentDetails to be posted along with the document to be uploaded. But none found! Check the post api call.");
        }

        docDetails.setDocHash(docHash);

        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(docDetails);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Unable to serialize IndiaCPDocumentDetails Object to JSON");
        }
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

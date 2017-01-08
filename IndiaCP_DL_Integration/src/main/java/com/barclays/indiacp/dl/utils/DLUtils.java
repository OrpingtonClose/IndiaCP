package com.barclays.indiacp.dl.utils;

import com.barclays.indiacp.dl.integration.IndiaCPProgramApi;
import com.barclays.indiacp.model.IndiaCPDocumentDetails;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.glassfish.jersey.media.multipart.MultiPartFeature;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Request;
import java.util.logging.Logger;

/**
 * Created by ritukedia on 08/01/17.
 */
public class DLUtils {

    public static WebTarget getDLRestEndPoint() {
        Client jerseyClient = ClientBuilder.newBuilder()
                .register(MultiPartFeature.class).build();

        return jerseyClient.target(DLConfig.DLConfigInstance().getDLRestEndpoint());
    }

    public static String getJSONString(Object jsonJavaObj) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(jsonJavaObj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Unable to serialize IndiaCPDocumentDetails Object to JSON");
        }
    }
}

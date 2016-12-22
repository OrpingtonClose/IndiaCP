package com.barclays.indiacp;

import javax.ws.rs.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.barclays.indiacp.utils.DLConfig;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.logging.LoggingFeature;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("indiacpprogram")
/*
 * mvn exec:exec -Dexec.executable="java" -Dexec.args="-classpath %classpath -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=1044 com.barclays.indiacp.Main"
 */
public class IndiaCPProgram {

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getIt() {
        return "Got it!";
    }

    @POST
    @Path("issueCPProgram")
    public Response issueCPProgram(String indiaCPProgramJSON)
    {
        Logger logger = Logger.getLogger(IndiaCPProgram.class.getName());
        Client jerseyClient = ClientBuilder.newClient();
        //Client jerseyClient = ClientBuilder.newClient(new ClientConfig().register(org.glassfish.jersey.jsonp.internal.JsonProcessingAutoDiscoverable.class));
//        Feature feature = new LoggingFeature(logger, Level.INFO, null, null);
//        jerseyClient.register(feature);
        WebTarget target = jerseyClient.target(DLConfig.DLConfigInstance().getDLRestEndpoint());

        return target.path("indiacpprogram/issueCPProgram").request().post(Entity.json(indiaCPProgramJSON));
    }

    @GET
    @Path("fetchAllCPProgram")
    public Response fetchAllCPProgram()
    {
        Logger logger = Logger.getLogger(IndiaCPProgram.class.getName());
        Client jerseyClient = ClientBuilder.newClient();
        //Client jerseyClient = ClientBuilder.newClient(new ClientConfig().register(org.glassfish.jersey.jsonp.internal.JsonProcessingAutoDiscoverable.class));
//        Feature feature = new LoggingFeature(logger, Level.INFO, null, null);
//        jerseyClient.register(feature);
        WebTarget target = jerseyClient.target(DLConfig.DLConfigInstance().getDLRestEndpoint());

        return target.path("indiacpprogram/fetchAllCPProgram").request().get();
    }
}

package com.barclays.indiacp;

import com.barclays.indiacp.dl.integration.IndiaCPIssue;
import com.barclays.indiacp.dl.integration.IndiaCPIssueFactory;
import com.barclays.indiacp.dl.integration.IndiaCPProgram;
import com.barclays.indiacp.dl.integration.IndiaCPProgramFactory;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;

/**
 * Main class.
 * mvn exec:exec -Dexec.executable="java" -Dexec.args="-classpath %classpath -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=1044 com.barclays.indiacp.Main"
 */
public class Main {
    // Base URI the Grizzly HTTP server will listen on
    public static final String BASE_URI = "http://localhost:8080/indiacp/";

    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
     * @return Grizzly HTTP server.
     */
    public static HttpServer startServer() {
        // create a resource config that scans for JAX-RS resources and providers
        // in com.barclays.indiacp.dl.integration package
        ResourceConfig rc = new ResourceConfig();
        rc.register(IndiaCPProgram.class).register(new AbstractBinder() {
            @Override
            protected void configure() {
                bindFactory(new IndiaCPProgramFactory()).to(IndiaCPProgram.class);
            }
        });
        rc.register(IndiaCPIssue.class).register(new AbstractBinder() {
            @Override
            protected void configure() {
                bindFactory(new IndiaCPIssueFactory()).to(IndiaCPIssue.class);
            }
        });
        rc.packages("com.barclays.indiacp.dl.integration");

        // create and start a new instance of grizzly http server
        // exposing the Jersey application at BASE_URI
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    /**
     * Main method.
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        final HttpServer server = startServer();
        System.out.println(String.format("Jersey app started with WADL available at "
                + "%sapplication.wadl\nHit enter to stop it...", BASE_URI));
        System.in.read();
        server.stop();
    }
}
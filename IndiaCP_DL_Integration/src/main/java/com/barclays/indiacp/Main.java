package com.barclays.indiacp;

import com.barclays.indiacp.dl.integration.IndiaCPIssueApi;
import com.barclays.indiacp.dl.integration.IndiaCPIssueApiFactory;
import com.barclays.indiacp.dl.integration.IndiaCPProgramApi;
import com.barclays.indiacp.dl.integration.IndiaCPProgramApiFactory;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.*;
import java.net.URI;

/**
 * Main class.
 * mvn exec:exec -Dexec.executable="java" -Dexec.args="-classpath %classpath -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=1044 com.barclays.indiacp.Main"
 */
public class Main {
    // Base URI the Grizzly HTTP server will listen on
    public static final String BASE_URI = "http://10.0.0.4:8181/indiacp/";

    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
     * @return Grizzly HTTP server.
     */
    public static HttpServer startServer() {
        // create a resource config that scans for JAX-RS resources and providers
        // in com.barclays.indiacp.dl.integration package
        ResourceConfig rc = new ResourceConfig();
        rc.register(IndiaCPProgramApi.class).register(new AbstractBinder() {
            @Override
            protected void configure() {
                bindFactory(new IndiaCPProgramApiFactory()).to(IndiaCPProgramApi.class);
            }
        });
        rc.register(IndiaCPIssueApi.class).register(new AbstractBinder() {
            @Override
            protected void configure() {
                bindFactory(new IndiaCPIssueApiFactory()).to(IndiaCPIssueApi.class);
            }
        });
        rc.register(MultiPartFeature.class);
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


    /*public static void main(String[] args) throws IOException {

    try{
        File initialFile = new File("E:\\SwitchBoard.zip");
        InputStream targetStream = new FileInputStream(initialFile);
        String theString = IOUtils.toString(targetStream, "UTF-8");
        final File tempFile = File.createTempFile("temp", ".zip");
        tempFile.deleteOnExit();
        FileOutputStream out = new FileOutputStream(tempFile);
        IOUtils.copy(targetStream, out);

    } catch (Exception ex)
        {
            throw new RuntimeException("File could not be uploaded.");
        }

    }*/

}

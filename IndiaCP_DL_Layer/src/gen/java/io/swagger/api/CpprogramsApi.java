package io.swagger.api;

import io.swagger.model.*;
import io.swagger.api.CpprogramsApiService;
import io.swagger.api.factories.CpprogramsApiServiceFactory;

import io.swagger.annotations.ApiParam;
import io.swagger.jaxrs.*;

import io.swagger.model.CPProgram;
import io.swagger.model.Error;

import java.util.List;
import io.swagger.api.NotFoundException;

import java.io.InputStream;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.*;

@Path("/cpprograms")

@Produces({ "application/json" })
@io.swagger.annotations.Api(description = "the cpprograms API")
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2016-12-21T10:49:10.288Z")
public class CpprogramsApi  {
   private final CpprogramsApiService delegate = CpprogramsApiServiceFactory.getCpprogramsApi();

    @GET
    @Path("/{issuer}")
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Fetch All CPPrograms for the current Issuer", notes = "Returns all the CP Programs for the current issuer ", response = CPProgram.class, responseContainer = "List", tags={ "Issuer", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "An array of CP Programs", response = CPProgram.class, responseContainer = "List"),
        
        @io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error", response = CPProgram.class, responseContainer = "List") })
    public Response fetchAllCPProgram(@ApiParam(value = "issuer id that uniquely maps to the issuer DL node",required=true) @PathParam("issuer") String issuer
,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.fetchAllCPProgram(issuer,securityContext);
    }
}

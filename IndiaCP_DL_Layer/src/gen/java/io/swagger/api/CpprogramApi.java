package io.swagger.api;

import io.swagger.model.*;
import io.swagger.api.CpprogramApiService;
import io.swagger.api.factories.CpprogramApiServiceFactory;

import io.swagger.annotations.ApiParam;
import io.swagger.jaxrs.*;

import io.swagger.model.CPProgram;
import io.swagger.model.Error;
import java.util.List;

import java.util.List;
import io.swagger.api.NotFoundException;

import java.io.InputStream;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.*;

@Path("/cpprogram")

@Produces({ "application/json" })
@io.swagger.annotations.Api(description = "the cpprogram API")
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2016-12-21T10:49:10.288Z")
public class CpprogramApi  {
   private final CpprogramApiService delegate = CpprogramApiServiceFactory.getCpprogramApi();

    @GET
    @Path("/{issuer}/{cpProgramId}")
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Get CP Program details by Id", notes = "This returns a single CP Program identified by an Id provided by the call ", response = CPProgram.class, tags={ "Issuer", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Returns a CP Program", response = CPProgram.class),
        
        @io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error", response = CPProgram.class) })
    public Response fetchCPProgram(@ApiParam(value = "issuer id that uniquely maps to the issuer DL node",required=true) @PathParam("issuer") String issuer
,@ApiParam(value = "CP Program ID that uniquely identifies the CP Program issued by the Issuer",required=true) @PathParam("cpProgramId") String cpProgramId
,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.fetchCPProgram(issuer,cpProgramId,securityContext);
    }
    @POST
    @Path("/{issuer}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Issue new CP Program", notes = "This creates a new CP Program with the details provided", response = CPProgram.class, tags={ "Issuer", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Creates a new CP Program", response = CPProgram.class),
        
        @io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error", response = CPProgram.class) })
    public Response issueCPProgram(@ApiParam(value = "Details of the CP Program to be Issued" ,required=true) List<CPProgram> cpprogramDetails
,@ApiParam(value = "Issuer id that uniquely maps to the issuer DL node",required=true) @PathParam("issuer") String issuer
,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.issueCPProgram(cpprogramDetails,issuer,securityContext);
    }
}

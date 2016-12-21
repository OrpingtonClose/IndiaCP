package io.swagger.api;

import io.swagger.model.*;
import io.swagger.api.CpissuesApiService;
import io.swagger.api.factories.CpissuesApiServiceFactory;

import io.swagger.annotations.ApiParam;
import io.swagger.jaxrs.*;

import io.swagger.model.CPIssue;
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

@Path("/cpissues")

@Produces({ "application/json" })
@io.swagger.annotations.Api(description = "the cpissues API")
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2016-12-21T10:49:10.288Z")
public class CpissuesApi  {
   private final CpissuesApiService delegate = CpissuesApiServiceFactory.getCpissuesApi();

    @GET
    @Path("/open/{entity}")
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Get All Open CP Issues for the given Issuer/Investor. Open CP Issues refers to the Issues that are yet to mature", notes = "This returns all the CP Issues under the umbrella CP Program identified by an Id provided by the call ", response = CPIssue.class, responseContainer = "List", tags={ "Issuer","Investor", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Get All Open CP Issues for the given Issuer/Investor. Open CP Issues refers to the Issues that are yet to mature", response = CPIssue.class, responseContainer = "List"),
        
        @io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error", response = CPIssue.class, responseContainer = "List") })
    public Response fetchAllCP(@ApiParam(value = "issuer or investor id that uniquely maps to the DL node",required=true) @PathParam("entity") String entity
,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.fetchAllCP(entity,securityContext);
    }
    @GET
    @Path("/{issuer}/{cpProgramId}")
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Get CP Issues allotted under a given CP Program", notes = "This returns all the CP Issues under the umbrella CP Program identified by an Id provided by the call ", response = CPIssue.class, responseContainer = "List", tags={ "Issuer", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Returns all CP Issues under the umbrella CP Program", response = CPIssue.class, responseContainer = "List"),
        
        @io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error", response = CPIssue.class, responseContainer = "List") })
    public Response fetchAllCPForCPProgram(@ApiParam(value = "issuer id that uniquely maps to the issuer DL node",required=true) @PathParam("issuer") String issuer
,@ApiParam(value = "CP Program ID that uniquely identifies the CP Program issued by the Issuer",required=true) @PathParam("cpProgramId") String cpProgramId
,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.fetchAllCPForCPProgram(issuer,cpProgramId,securityContext);
    }
}

package io.swagger.api;

import io.swagger.model.*;
import io.swagger.api.CpissueApiService;
import io.swagger.api.factories.CpissueApiServiceFactory;

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

@Path("/cpissue")

@Produces({ "application/json" })
@io.swagger.annotations.Api(description = "the cpissue API")
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2016-12-21T10:49:10.288Z")
public class CpissueApi  {
   private final CpissueApiService delegate = CpissueApiServiceFactory.getCpissueApi();

    @GET
    @Path("/{entity}/{cpIssueId}")
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Get All Open CP Issues for the given Issuer/Investor. Open CP Issues refers to the Issues that are yet to mature", notes = "This returns all the CP Issues under the umbrella CP Program identified by an Id provided by the call ", response = CPIssue.class, tags={ "Issuer","Investor", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Get All Open CP Issues for the given Issuer/Investor. Open CP Issues refers to the Issues that are yet to mature", response = CPIssue.class),
        
        @io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error", response = CPIssue.class) })
    public Response fetchCP(@ApiParam(value = "issuer or investor id that uniquely maps to the DL node",required=true) @PathParam("entity") String entity
,@ApiParam(value = "Unique identifier of the CP Issue to be fetched",required=true) @PathParam("cpIssueId") String cpIssueId
,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.fetchCP(entity,cpIssueId,securityContext);
    }
}

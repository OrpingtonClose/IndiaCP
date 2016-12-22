package io.swagger.api;

import io.swagger.model.*;
import io.swagger.api.TransactionhistoryApiService;
import io.swagger.api.factories.TransactionhistoryApiServiceFactory;

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

@Path("/transactionhistory")

@Produces({ "application/json" })
@io.swagger.annotations.Api(description = "the transactionhistory API")
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2016-12-21T10:49:10.288Z")
public class TransactionhistoryApi  {
   private final TransactionhistoryApiService delegate = TransactionhistoryApiServiceFactory.getTransactionhistoryApi();

    @GET
    @Path("/{issuer}/{cpProgramId}")
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Complete audit log of all changes/versions of given CP Program", notes = "A given CP Program once initiated undergoes various changes as it progresses through the trade lifecycle of generating ISIN, generating Deal Confirmations with each identified Investor, getting IPA Verification till the final settlement of all Deals and followed by redemption of the CP at the Maturity Date. This API will return this complete log history.", response = CPProgram.class, responseContainer = "List", tags={ "User", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "An array of all versions of the given CP Program", response = CPProgram.class, responseContainer = "List"),
        
        @io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error", response = CPProgram.class, responseContainer = "List") })
    public Response transactionhistoryIssuerCpProgramIdGet(@ApiParam(value = "issuer id that uniquely maps to the DL node",required=true) @PathParam("issuer") String issuer
,@ApiParam(value = "CP Program ID that uniquely identifies the CP Program issued by the Issuer",required=true) @PathParam("cpProgramId") String cpProgramId
,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.transactionhistoryIssuerCpProgramIdGet(issuer,cpProgramId,securityContext);
    }
}

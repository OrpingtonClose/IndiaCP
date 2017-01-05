package com.barclays.indiacp.quorum.contract.api;

import com.barclays.indiacp.model.IndiaCPIssue;
import com.barclays.indiacp.model.IndiaCPProgram;
import com.barclays.indiacp.quorum.utils.CakeshopUtils;
import com.jpmorgan.cakeshop.client.model.Contract;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ritukedia on 23/12/16.
 */
@Path("indiacpprogram")
public class IndiaCPProgramController {

    Request request;
    String userId;

    @POST
    @Path("issueCPProgram")
    @Consumes(MediaType.APPLICATION_JSON)
    public String issueCPProgram(IndiaCPProgram indiaCPProgramArgs) {
        return CakeshopUtils.createContract(this.getClass().getSimpleName(), indiaCPProgramArgs);
    }

    @GET
    @Path("fetchAllCPProgram")
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList<IndiaCPProgram>  fetchAllCPProgram() {
        //fetch all contract instances of IndiaCPProgramController Contracts
        List<Contract> contractList = CakeshopUtils.listContractsByName(this.getClass().getSimpleName());

        //fetch details of all IndiaCPProgramController Contract instances
        ArrayList<IndiaCPProgram> indiaCPProgramArrayList = new ArrayList<>();

        //TODO find all method names which are required to fetch all the contents of CPPrograms
        //Need to check if the code works
        /*SolidityContract contract = null;
        contract.getContractABI().findFunction(new org.apache.commons.collections4.Predicate<ContractABI.Function>() {
            @Override
            public boolean evaluate(ContractABI.Function f) {
                return f.name.startsWith("fetchCPProgram");
            }
        });*/

        //Temporarily hardcoded all fetch methods
        String[] readMethodNames = {"fetchCPProgramTradeDetails", "fetchCPProgramDocuments",
                "fetchCPProgramParties", "fetchCPProgramStatus"};

        for(Contract contract: contractList){
            indiaCPProgramArrayList.add(CakeshopUtils.readContract(this.getClass().getSimpleName(), contract.getAddress(), IndiaCPProgram.class, readMethodNames));
        }

        return indiaCPProgramArrayList;
    }

    @GET
    @Path("fetchCPProgram/{cpProgramId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response fetchCPProgram(@PathParam("cpProgramId") String cpProgramId) {
        //TODO Get contract address from mapping service
        String contractAddress = cpProgramId;

        String[] readMethodNames = {"fetchCPProgramTradeDetails", "fetchCPProgramDocuments",
                "fetchCPProgramParties", "fetchCPProgramStatus"};

        IndiaCPProgram indiaCPProgram = CakeshopUtils.readContract(this.getClass().getSimpleName(), contractAddress, IndiaCPProgram.class, readMethodNames);
        return Response.status(Response.Status.OK).build();

    }


    @POST
    @Path("addISIN/{cpProgramId}/{isin}")
    public Response addISIN(@PathParam("cpProgramId") String cpProgramId) {
        return Response.status(Response.Status.OK).build();

    }

    @POST
    @Path("issueCP/{cpProgramId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response issueCP(@PathParam("cpProgramId") String cpProgramId, IndiaCPIssue cpIssue) {
        String contractAddress = CakeshopUtils.createContract(this.getClass().getName(), cpIssue);
        return Response.status(Response.Status.OK).build();

    }

    @POST
    @Path("addISINGenerationDocs/{cpProgramId}/{docHash}/{docStatus}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    //@UploadsDocument
    public Response addISINGenerationDocs(@PathParam("cpProgramId") String cpProgramId
            , @PathParam("docStatus") String docStatus
            , InputStream uploadedInputStream) {
        return Response.status(Response.Status.OK).build();

    }

    @Context
    public void setRequest(Request request) {
        this.request = request;
        this.userId = ""; //TODO: Parse the userId from the Request Header
    }

}

package com.barclays.indiacp.quorum.contract.api;

import com.barclays.indiacp.model.CPIssue;
import com.barclays.indiacp.model.CPProgram;
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
public class IndiaCPProgram {

    Request request;
    String userId;

    @POST
    @Path("issueCPProgram")
    @Consumes(MediaType.APPLICATION_JSON)
    public String issueCPProgram(CPProgram cpProgramArgs) {
        return CakeshopUtils.createContract(this.getClass().getSimpleName(), cpProgramArgs);
    }

    @GET
    @Path("fetchAllCPProgram")
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList<CPProgram>  fetchAllCPProgram() {
        //fetch all contract instances of IndiaCPProgram Contracts
        List<Contract> contractList = CakeshopUtils.listContractsByName(this.getClass().getSimpleName());

        //fetch details of all IndiaCPProgram Contract instances
        ArrayList<CPProgram> cpProgramArrayList = new ArrayList<>();

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
            cpProgramArrayList.add(CakeshopUtils.readContract(this.getClass().getSimpleName(), contract.getAddress(), CPProgram.class, readMethodNames));
        }

        return cpProgramArrayList;
    }

    @GET
    @Path("fetchCPProgram/{cpProgramId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response fetchCPProgram(@PathParam("cpProgramId") String cpProgramId) {
        //TODO Get contract address from mapping service
        String contractAddress = cpProgramId;

        String[] readMethodNames = {"fetchCPProgramTradeDetails", "fetchCPProgramDocuments",
                "fetchCPProgramParties", "fetchCPProgramStatus"};

        CPProgram cpProgram = CakeshopUtils.readContract(this.getClass().getSimpleName(), contractAddress, CPProgram.class, readMethodNames);
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
    public Response issueCP(@PathParam("cpProgramId") String cpProgramId, CPIssue cpIssue) {
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

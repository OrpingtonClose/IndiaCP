package com.barclays.indiacp.quorum.contract.api;

import com.barclays.indiacp.config.JsonMethodArgumentResolver;
import com.barclays.indiacp.model.IndiaCPDocumentDetails;
import com.barclays.indiacp.model.IndiaCPIssue;
import com.barclays.indiacp.model.IndiaCPProgram;
import com.barclays.indiacp.quorum.utils.CakeshopUtils;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jpmorgan.cakeshop.client.model.Contract;
import org.glassfish.jersey.media.multipart.FormDataParam;

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
public class IndiaCPProgramController extends IndiaCPContractController {

    Request request;
    String userId; // is this the owner's address? should get from OAuth header?

    String [] METHODS = {"fetchCPProgramTradeDetails", "fetchCPProgramDocuments", "fetchCPProgramParties", "fetchCPProgramStatus", "issueCP"}; //temporarily hardcoded

    @POST
    @Path("issueCPProgram")
    @Consumes(MediaType.APPLICATION_JSON)
    public String issueCPProgram(IndiaCPProgram indiaCPProgramArgs) {
        String addr = CakeshopUtils.createContract(this.getClass().getSimpleName().replaceFirst("Controller", ""), indiaCPProgramArgs, userId);
        System.out.println("Newly created contract mined at: "+addr);
        return addr;
    }

    @GET
    @Path("fetchAllCPProgram")
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList<IndiaCPProgram>  fetchAllCPProgram() {
        //fetch all contract instances of IndiaCPProgramController Contracts
        List<Contract> contractList = CakeshopUtils.listContractsByName(this.getClass().getSimpleName().replaceFirst("Controller", ""));

        //fetch details of all IndiaCPProgramController Contract instances
        ArrayList<IndiaCPProgram> indiaCPProgramArrayList = new ArrayList<>();

        //TODO find all method names which are required to fetch all the contents of CPPrograms
        //Partially working
        /*SolidityContractCode contract1 = SolidityContractCode.getSingleInstance(this.getClass().getSimpleName().replaceFirst("Controller", ""));
        ContractABI.Function b = contract1.getContractABI().findFunction(new Predicate<ContractABI.Function>() {
            @Override
            public boolean evaluate(ContractABI.Function f) {
                return f.name.startsWith("fetchCPProgram");
            }
        });
        */
        //Temporarily hardcoded all fetch methods
        String[] readMethodNames = METHODS;

        for(Contract contract: contractList){
            //indiaCPProgramArrayList.add(Optional.ofNullable(CakeshopUtils.readContract(this.getClass().getSimpleName().replaceFirst("Controller", ""), contract.getAddress(), IndiaCPProgram.class, readMethodNames)).orElse(null));
            IndiaCPProgram temp = CakeshopUtils.readContract(this.getClass().getSimpleName().replaceFirst("Controller", ""), contract.getAddress(), IndiaCPProgram.class, readMethodNames);
            if(temp!=null){
                indiaCPProgramArrayList.add(temp);
            }
        }
        return indiaCPProgramArrayList;
    }

    @GET
    @Path("fetchCPProgram/{cpProgramId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public IndiaCPProgram fetchCPProgram(@PathParam("cpProgramId") String cpProgramId) {
        //TODO Get contract address from mapping service
        String contractAddress = cpProgramId;

        String[] readMethodNames = METHODS;

        return CakeshopUtils.readContract(this.getClass().getSimpleName(), contractAddress, IndiaCPProgram.class, readMethodNames);
    }


    @POST
    @Path("addISIN/{cpProgramId}/{isin}")
    public Response addISIN(@PathParam("cpProgramId") String cpProgramId) {
        return Response.status(Response.Status.OK).build();

    }

    @POST
    @Path("issueCP/{cpProgramId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public String issueCP(@PathParam("cpProgramId") String cpProgramId, IndiaCPIssue cpIssue) {
        IndiaCPProgram cpProg = fetchCPProgram(cpProgramId);
        String cpProgAddr = resolveAddrFromID(cpProgramId);
        String issuedCpAddr="";

        if (cpIssue.getRate() <= cpProg.getProgramAllocatedValue()) {
            issuedCpAddr = new IndiaCPIssueController().issueCPIssue(cpIssue, cpProgAddr); //is this the correct way to call this?
            if (null != issuedCpAddr && ""!=issuedCpAddr) {
                Object[] args = {cpIssue.getRate(), issuedCpAddr};
                CakeshopUtils.transactContract(cpProgAddr, "issueCP", args); //embed issueAddr in cpProg, reduce allocation value
            }
            else {
                // else: throw exception, log error, whatever
                issuedCpAddr = "ERROR";
            }
        }
        return issuedCpAddr;
    }

    @POST
    @Path("addISINGenerationDocs/{cpProgramId}/{docHash}/{docStatus}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    //@UploadsDocument
    public Response addISINGenerationDocs(@PathParam("cpProgramId") String cpProgramId, @PathParam("docStatus") String docStatus, InputStream uploadedInputStream) {
        // what is this function supposed to do?
        return Response.status(Response.Status.OK).build();
    }

    @Context
    public void setRequest(Request request) {
        this.request = request;
        this.userId = ""; //TODO: Parse the userId from the Request Header
    }

    @POST
    @Path("addDocs/{docType}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    //Embeds document in contract
    public Response addDocs(@JsonProperty("documentDetails") IndiaCPDocumentDetails docDetails, @PathParam("docType") String docType) {
        String contractAddr = resolveAddrFromID(docDetails.getCpProgramId());
        // use docType to determine methodName
        String txid = CakeshopUtils.transactContract(contractAddr, "setIsinGenerationRequestDocId", new Object[] {docDetails.getDocHash()});
        return Response.status(Response.Status.OK).build();
    }

    public String resolveAddrFromID(String progId) {
        return "TODO";
    }



}

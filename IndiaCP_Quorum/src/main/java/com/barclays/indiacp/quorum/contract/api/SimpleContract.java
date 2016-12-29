package com.barclays.indiacp.quorum.contract.api;

import com.barclays.indiacp.quorum.contract.code.SSContractCode;
import com.barclays.indiacp.quorum.utils.CakeshopUtils;
import com.jpmorgan.cakeshop.client.model.req.ContractCreateCommand;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by ritukedia on 28/12/16.
 */
@Path("sscontract")
public class SimpleContract {

    @POST
    @Path("create")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(String jsonBody) {

        String contractAddress = CakeshopUtils.createContract(this.getClass().getName(), null);
        return Response.status(Response.Status.OK).build();
    }

    public ContractCreateCommand getContractCreateCommand() {
        ContractCreateCommand contractCreateCommand = new ContractCreateCommand();
        contractCreateCommand.setCode(SSContractCode.getSingleInstance().getContractCode());
        contractCreateCommand.setBinary(SSContractCode.getSingleInstance().getContractBinary());
        contractCreateCommand.setCodeType(SSContractCode.getSingleInstance().getCodeType());
        contractCreateCommand.setFrom("0x2e219248f44546d966808cdd20cb6c36df6efa82");
        //contractCreateCommand.setArgs(new Object[]);
        return contractCreateCommand;
    }

}

package com.barclays.indiacp.quorum.contract;

import com.barclays.indiacp.quorum.contract.rest.CakeshopUtils;
import com.jpmorgan.cakeshop.client.api.ContractApi;
import com.jpmorgan.cakeshop.client.model.Contract;
import com.jpmorgan.cakeshop.client.model.TransactionResult;
import com.jpmorgan.cakeshop.client.model.req.ContractCreateCommand;
import com.jpmorgan.cakeshop.client.model.res.APIData;
import com.jpmorgan.cakeshop.client.model.res.APIResponse;

import javax.ws.rs.*;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by ritukedia on 28/12/16.
 */
@Path("sscontract")
public class SSContract {

    private ContractApi contractApi;

    public SSContract() {
        contractApi = CakeshopUtils.getCakeshopContractApi();
    }

    @POST
    @Path("create")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(String jsonBody) {
        APIResponse<APIData<TransactionResult>, TransactionResult> res = contractApi.create(getContractCreateCommand());

        return null;
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

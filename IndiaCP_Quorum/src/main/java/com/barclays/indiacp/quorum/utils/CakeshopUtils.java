package com.barclays.indiacp.quorum.utils;

import com.barclays.indiacp.quorum.contract.code.SolidityContractCode;
import com.barclays.indiacp.quorum.contract.code.SolidityContractCodeFactory;
import com.google.common.util.concurrent.ListenableFuture;
import com.jpmorgan.cakeshop.client.ClientManager;
import com.jpmorgan.cakeshop.client.api.ContractApi;
import com.jpmorgan.cakeshop.client.model.Contract;
import com.jpmorgan.cakeshop.client.model.Transaction;
import com.jpmorgan.cakeshop.client.model.TransactionResult;
import com.jpmorgan.cakeshop.client.model.req.ContractCompileCommand;
import com.jpmorgan.cakeshop.client.model.req.ContractCreateCommand;
import com.jpmorgan.cakeshop.client.model.res.APIData;
import com.jpmorgan.cakeshop.client.model.res.APIResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by ritukedia on 28/12/16.
 */
public class CakeshopUtils {

    private static ClientManager cakeshopManager;
    private static ContractApi contractApi;

    static {
        // setup cakeshop manager
        cakeshopManager = ClientManager.create("http://localhost:8080/cakeshop");
        contractApi = cakeshopManager.getClient(ContractApi.class);
    }


    //Takes solidity string, returns compiled binary
    public static Contract compileSolidity(String contractCode) {
        ContractCompileCommand c = new ContractCompileCommand().code(contractCode).codeType(Contract.CodeTypeEnum.SOLIDITY).optimize(true);
        APIResponse<List<APIData<Contract>>, Contract> res = contractApi.compile(c);
        APIData<Contract> result = res.getApiData().get(0);
        return result.getAttributes();
    }

    public static String createContract(String contractName, Object contractModel) {
        APIResponse<APIData<TransactionResult>, TransactionResult> res = contractApi.create(getContractCreateCommand(contractName, contractModel));
        final ListenableFuture<Transaction> txFuture = cakeshopManager.waitForTx(res.getData());
        txFuture.addListener(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("tx committed:\n" + txFuture.get().toString());
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }, MoreExecutors.directExecutor());
        //TODO: fetch contract address from the APIResponse
        return "contract address";
    }

    public static ContractCreateCommand getContractCreateCommand(String contractName, Object contractModel) {
        SolidityContractCode contractCode = SolidityContractCodeFactory.getInstance(contractName);
        ContractCreateCommand contractCreateCommand = new ContractCreateCommand();
        contractCreateCommand.setCode(contractCode.getContractCode());
        contractCreateCommand.setBinary(contractCode.getContractBinary());
        contractCreateCommand.setCodeType(contractCode.getCodeType());
        contractCreateCommand.setFrom("0x2e219248f44546d966808cdd20cb6c36df6efa82");
        contractCreateCommand.setArgs(contractModel == null? null: contractCode.getConstructorArgs(contractModel));
        return contractCreateCommand;

    }

    public static List<Contract> listContractsByName(String nameFilter){
        APIResponse<List<APIData<Contract>>, Contract> apiResponse = getCakeshopContractApi().list();

        ArrayList<Contract> filteredContracts = new ArrayList<>();
        for(Contract contract: apiResponse.getDataAsList()){
            contract.getName().equalsIgnoreCase(nameFilter);
            filteredContracts.add(contract);
        }
        return apiResponse.getDataAsList();
    }

    public static Object getContractState(String contractAddress) {

        APIResponse<APIData<Contract>, Contract> a = getCakeshopContractApi().get(contractAddress);

        //TODO find state using read calls
        return new Object();
    }


}

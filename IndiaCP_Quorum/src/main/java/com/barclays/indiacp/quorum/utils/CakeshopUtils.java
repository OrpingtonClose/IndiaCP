package com.barclays.indiacp.quorum.utils;

import com.barclays.indiacp.quorum.contract.code.SolidityContract;
import com.barclays.indiacp.quorum.contract.code.SolidityContractCodeFactory;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import com.jpmorgan.cakeshop.client.ClientManager;
import com.jpmorgan.cakeshop.client.api.ContractApi;
import com.jpmorgan.cakeshop.client.api.TransactionApi;
import com.jpmorgan.cakeshop.client.model.Contract;
import com.jpmorgan.cakeshop.client.model.Transaction;
import com.jpmorgan.cakeshop.client.model.TransactionResult;
import com.jpmorgan.cakeshop.client.model.req.ContractCompileCommand;
import com.jpmorgan.cakeshop.client.model.req.ContractCreateCommand;
import com.jpmorgan.cakeshop.client.model.req.ContractMethodCallCommand;
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
    private static TransactionApi transactionApi;

    static {
        // setup cakeshop manager
        cakeshopManager = ClientManager.create("http://52.172.42.128:8080/cakeshop");
        contractApi = cakeshopManager.getClient(ContractApi.class);
        transactionApi = cakeshopManager.getClient(TransactionApi.class);
    }

    public static String getAddrFromTxId (String txnID) {
        return transactionApi.get(txnID).getData().getContractAddress();
    }

    //Takes solidity string, returns compiled binary
    public static Contract compileSolidity(String contractCode) {
        ContractCompileCommand c = new ContractCompileCommand().code(contractCode).codeType(Contract.CodeTypeEnum.SOLIDITY).optimize(true);
        APIResponse<List<APIData<Contract>>, Contract> res = contractApi.compile(c);
        APIData<Contract> result = res.getApiData().get(0);
        return result.getAttributes();
    }

    //returns address of newly mined contract
    public static String createContract(String contractName, Object contractModel) {
        APIResponse<APIData<TransactionResult>, TransactionResult> res = contractApi.create(getContractCreateCommand(contractName, contractModel));
        String txnID = res.getData().getId();
        //wait till commit happens - TODO: check if this commit is to contractregistry or actual chain.
        final ListenableFuture<Transaction> txFuture = cakeshopManager.waitForTx(txnID);
        txFuture.addListener(() -> {
                try { System.out.println("Transaction committed:\n" + txFuture.get().toString()); } //use as logger
                catch (InterruptedException|ExecutionException e) { e.printStackTrace(); }
                },
                MoreExecutors.directExecutor());

        return getAddrFromTxId(txnID);
    }

    // creates argument object to create contract.
    public static ContractCreateCommand getContractCreateCommand(String contractName, Object contractModel) {
        SolidityContract contractCode = SolidityContractCodeFactory.getInstance(contractName);
        ContractCreateCommand contractCreateCommand = new ContractCreateCommand();
        contractCreateCommand.setCode(contractCode.getContractCode());
        contractCreateCommand.setBinary(contractCode.getContractBinary());
        contractCreateCommand.setCodeType(contractCode.getCodeType());
        contractCreateCommand.setFrom("0x2e219248f44546d966808cdd20cb6c36df6efa82");
        contractCreateCommand.setArgs(contractModel == null? null: IndiaCPContractUtils.getConstructorArgs(contractCode, contractModel));
        return contractCreateCommand;

    }

    public static List<Contract> listContractsByName(String nameFilter){
        APIResponse<List<APIData<Contract>>, Contract> apiResponse = contractApi.list();

        ArrayList<Contract> filteredContracts = new ArrayList<>();
        for(Contract contract: apiResponse.getDataAsList()){
            if (contract.getName().equalsIgnoreCase(nameFilter)) {
                filteredContracts.add(contract);
            }
        }
        return filteredContracts;
    }

    public static Object getContractState(String contractAddress) {

        APIResponse<APIData<Contract>, Contract> a = contractApi.get(contractAddress);

        //TODO find state using read calls
        return new Object();
    }


    public static <T>  T readContract(String contractName, String contractAddress, Class<T> contractModel, String... readMethodNames) {

        T contractModelObject = null;
        for(int i=0; i<readMethodNames.length; i++){
        APIResponse<List<Object>, Object> apiResponse = contractApi.read(getContractMethodCallCommand(contractAddress, readMethodNames[i]));

        contractModelObject = IndiaCPContractUtils.populateContractModel(contractModelObject, SolidityContractCodeFactory.getInstance(contractName),
                readMethodNames[i],
                contractModel,
                apiResponse.getApiData());
        }
        return contractModelObject;
    }

    public static ContractMethodCallCommand getContractMethodCallCommand(String contractAddress, String methodName) {
        ContractMethodCallCommand contractMethodCallCommand = new ContractMethodCallCommand();
        contractMethodCallCommand.setAddress(contractAddress);
        contractMethodCallCommand.setMethod(methodName);
        return contractMethodCallCommand;
    }
}

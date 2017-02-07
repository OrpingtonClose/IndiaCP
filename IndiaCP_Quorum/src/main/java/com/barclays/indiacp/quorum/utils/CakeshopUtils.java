package com.barclays.indiacp.quorum.utils;

import com.barclays.indiacp.quorum.contract.code.SolidityContract;
import com.barclays.indiacp.quorum.contract.code.SolidityContractCodeFactory;
import com.barclays.indiacp.util.StringUtils;
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
import feign.FeignException;
import io.ipfs.api.IPFS;
import io.ipfs.api.MerkleNode;
import io.ipfs.api.NamedStreamable;
import io.ipfs.multihash.Multihash;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.WebAsyncTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

/**
 * Created by ritukedia on 28/12/16.
 */
public class CakeshopUtils {

    private static ClientManager cakeshopManager;
    private static ContractApi contractApi;
    private static TransactionApi transactionApi;
    private static IPFS ipfs;
    static {
        // setup cakeshop manager
        cakeshopManager = ClientManager.create("http://52.172.49.221:8080/cakeshop");
        contractApi = cakeshopManager.getClient(ContractApi.class);
        transactionApi = cakeshopManager.getClient(TransactionApi.class);

        //needs an object factory
        ipfs = new IPFS("/ip4/52.172.49.221/tcp/5001");
//        ipfs = new IPFS("http://52.172.49.221", 5001);
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
    public static String createContract(String contractName, Object contractModel, String owner) {
        APIResponse<APIData<TransactionResult>, TransactionResult> res = contractApi.create(getContractCreateCommand(contractName, contractModel, owner));
        return runAsyncTransaction(res);
    }

    // creates argument object to create contract.
    public static ContractCreateCommand getContractCreateCommand(String contractName, Object contractModel, String owner) {
        SolidityContract contractCode = SolidityContractCodeFactory.getInstance(contractName);
        ContractCreateCommand contractCreateCommand = new ContractCreateCommand();
        contractCreateCommand.setCode(contractCode.getContractCode());
        contractCreateCommand.setBinary(contractCode.getContractBinary());
        contractCreateCommand.setCodeType(contractCode.getCodeType());
        contractCreateCommand.setFrom(owner); //TODO
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

    public static <T>  T readContract(String contractName, String contractAddress, Class<T> contractModel, String... readMethodNames) {
        T contractModelObject = null;
        for(int i=0; i<readMethodNames.length; i++){
            try {
                APIResponse<List<Object>, Object> apiResponse = contractApi.read(getContractMethodCallCommand(contractAddress, readMethodNames[i]));

            contractModelObject = IndiaCPContractUtils.populateContractModel(contractModelObject, SolidityContractCodeFactory.getInstance(contractName),
                    readMethodNames[i],
                    contractModel,
                    apiResponse.getApiData());
            }catch(FeignException e){
                System.out.println("Could not read method " + readMethodNames[i]+ "for contract " + contractName + " at " + contractAddress);
                //e.printStackTrace();
            }
        }
        return contractModelObject;
    }


    public static String transactContract(String contractAddress, String methodName, Object[] args) {
        ContractMethodCallCommand call = getContractMethodCallCommand(contractAddress, methodName);
        call.setArgs(args);
        APIResponse<APIData<TransactionResult>, TransactionResult> apiResponse = contractApi.transact(call);
        return runAsyncTransaction(apiResponse);
    }


    //returns txnID of mined async transaction
    public static String runAsyncTransaction(APIResponse<APIData<TransactionResult>,TransactionResult> res) {
        String txnID = res.getData().getId();
        //wait till commit happens - TODO: check if this commit is to contractregistry or actual chain.
        final ListenableFuture<Transaction> txFuture = cakeshopManager.waitForTx(txnID);
        txFuture.addListener(() -> {
                    try { System.out.println("Transaction committed:\n" + txFuture.get().toString()); } //use as logger
                    catch (InterruptedException|ExecutionException e) { e.printStackTrace(); }
                },
                MoreExecutors.directExecutor());
        return txnID;
    }

    public static ContractMethodCallCommand getContractMethodCallCommand(String contractAddress, String methodName) {
        ContractMethodCallCommand contractMethodCallCommand = new ContractMethodCallCommand();
        contractMethodCallCommand.setAddress(contractAddress);
        contractMethodCallCommand.setMethod(methodName);
        return contractMethodCallCommand;
    }

    // Uploads document to IPFS and returns hashcode
    public static String uploadDocumentToIPFS(InputStream inputFileStream) throws RuntimeException {
        final String PREFIX = StringUtils.toString(System.currentTimeMillis());
        final String SUFFIX = "garbage";
        File tempFile = null;
        try {
            tempFile = File.createTempFile(PREFIX, SUFFIX);
        } catch (IOException e) {
            System.out.println("Temp file creation failed");
            throw new RuntimeException();
        }
        tempFile.deleteOnExit();

        try {
            IOUtils.copy(inputFileStream, new FileOutputStream(tempFile));
        } catch (IOException e) {
            System.out.println("Could not persist input stream");
            throw new RuntimeException();
        }

        MerkleNode fileAddResult = null;
        try {
            fileAddResult = ipfs.add(new NamedStreamable.FileWrapper(tempFile));
        } catch (IOException e) {
            System.out.println("Upload to ipfs failed");
            throw new RuntimeException();
        }
        return fileAddResult.hash.toBase58();
    }

    // Downloads doc from IPFS given a hash
    public static void downloadDocumentFromIPFS(String dochash, String destPath) throws IOException {
        FileOutputStream fos = new FileOutputStream(destPath);
        byte[] fileContents = ipfs.cat(Multihash.fromBase58(dochash));
        fos.write(fileContents);
        fos.close();
    }

    public static String uploadDocumentToIPFSTEST() throws RuntimeException {
        final String PREFIX = StringUtils.toString(System.currentTimeMillis());
        final String SUFFIX = "garbage";
        File tempFile = null;
        try {
            tempFile = File.createTempFile(PREFIX, SUFFIX);
        } catch (IOException e) {
            System.out.println("Temp file creation failed");
            throw new RuntimeException();
        }
        tempFile.deleteOnExit();

        try {
//            IOUtils.copy(inputFileStream, new FileOutputStream(tempFile));
            tempFile = new File("/home/surajman/a.txt");
        } catch (Exception e) {
            System.out.println("Could not persist input stream");
            throw new RuntimeException();
        }

        MerkleNode fileAddResult = null;
        try {
            fileAddResult = ipfs.add(new NamedStreamable.FileWrapper(tempFile));
        } catch (IOException e) {
            System.out.println("Upload to ipfs failed");
            throw new RuntimeException();
        }
        return fileAddResult.hash.toBase58();
    }

}

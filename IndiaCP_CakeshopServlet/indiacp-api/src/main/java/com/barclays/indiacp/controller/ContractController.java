package com.barclays.indiacp.controller;

import com.barclays.indiacp.config.JsonMethodArgumentResolver;
import com.barclays.indiacp.error.APIException;
import com.barclays.indiacp.model.*;
import com.barclays.indiacp.service.ContractRegistryService;
import com.barclays.indiacp.service.ContractService;
import com.barclays.indiacp.model.APIData;
import com.barclays.indiacp.model.APIError;
import com.barclays.indiacp.model.APIResponse;
import com.barclays.indiacp.model.Contract;
import com.barclays.indiacp.model.ContractABI;
import com.barclays.indiacp.model.Transaction;
import com.barclays.indiacp.model.TransactionRequest;
import com.barclays.indiacp.model.TransactionResult;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import com.barclays.indiacp.util.StringUtils;
import io.ipfs.multihash.Multihash;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.WebAsyncTask;

import io.ipfs.api.*;

@RestController
@RequestMapping(value = "/api/contract",
    method = RequestMethod.POST,
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE)
public class ContractController extends BaseController {

    private static final String DEFAULT_CODE_TYPE = "solidity";

    @Autowired
    private ContractService contractService;

	@Autowired
	private ContractRegistryService contractRegistry;


    @RequestMapping("/get")
    public ResponseEntity<APIResponse> getContract(
            @JsonMethodArgumentResolver.JsonBodyParam String address) throws APIException {

        Contract contract = contractService.get(address);

        APIResponse res = new APIResponse();

        if (contract != null) {
            res.setData(toAPIData(contract));
            return new ResponseEntity<>(res, HttpStatus.OK);
        }

        APIError err = new APIError();
        err.setStatus("404");
        err.setTitle("Contract not found");
        res.addError(err);
        return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
    }

    @RequestMapping("/compile")
    public ResponseEntity<APIResponse> compile(
            @JsonMethodArgumentResolver.JsonBodyParam String code,
            @JsonMethodArgumentResolver.JsonBodyParam(defaultValue=DEFAULT_CODE_TYPE) String code_type,
            @JsonMethodArgumentResolver.JsonBodyParam(required=false) Boolean optimize) throws APIException {

        List<Contract> contracts = contractService.compile(code, ContractService.CodeType.valueOf(code_type), optimize);

        APIResponse res = new APIResponse();

        if (contracts != null) {
            res.setData(toAPIData(contracts));
            return new ResponseEntity<>(res, HttpStatus.OK);
        }

        APIError err = new APIError();
        err.setStatus("400");
        err.setTitle("Bad Request");
        res.addError(err);
        return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
    }

    @RequestMapping("/create")
    public ResponseEntity<APIResponse> create(
            @JsonMethodArgumentResolver.JsonBodyParam String from,
            @JsonMethodArgumentResolver.JsonBodyParam String code,
            @JsonMethodArgumentResolver.JsonBodyParam(defaultValue=DEFAULT_CODE_TYPE) String code_type,
            @JsonMethodArgumentResolver.JsonBodyParam(required=false) Object[] args,
            @JsonMethodArgumentResolver.JsonBodyParam(required=false) String binary,
            @JsonMethodArgumentResolver.JsonBodyParam(required=false) Boolean optimize,
            @JsonMethodArgumentResolver.JsonBodyParam final String privateFrom,
            @JsonMethodArgumentResolver.JsonBodyParam final List<String> privateFor) throws APIException {

        TransactionResult tx = contractService.create(from, code, ContractService.CodeType.valueOf(code_type), args, binary,
                privateFrom, privateFor);

        APIResponse res = new APIResponse();

        if (tx != null) {
            res.setData(tx.toAPIData());
            return new ResponseEntity<>(res, HttpStatus.OK);
        }

        APIError err = new APIError();
        err.setStatus("400");
        err.setTitle("Bad Request");
        res.addError(err);
        return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
    }

    @RequestMapping("/list")
    public ResponseEntity<APIResponse> list() throws APIException {
        List<Contract> contracts = contractService.list();
        APIResponse res = new APIResponse();
        res.setData(toAPIData(contracts));

        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @RequestMapping("/read")
    public ResponseEntity<APIResponse> read(
            @JsonMethodArgumentResolver.JsonBodyParam String from,
            @JsonMethodArgumentResolver.JsonBodyParam String address,
            @JsonMethodArgumentResolver.JsonBodyParam String method,
            @JsonMethodArgumentResolver.JsonBodyParam Object[] args,
            @JsonMethodArgumentResolver.JsonBodyParam(required=false) Object blockNumber) throws APIException {

        Object result = contractService.read(createTransactionRequest(from, address, method, args, true, blockNumber));
        APIResponse res = new APIResponse();
        res.setData(result);

        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    private TransactionRequest createTransactionRequest(String from, String address, String method, Object[] args, boolean isRead, Object blockNumber) throws APIException {
        ContractABI abi = contractService.get(address).getContractAbi();
        if (abi == null) {
            throw new APIException("Contract adddress " + address + " is not in the registry");
        }

        ContractABI.Function func = abi.getFunction(method);
        if (func == null) {
            throw new APIException("Method '" + method + "' does not exist in contract at " + address);
        }

        args = decodeArgs(func, args);

        return new TransactionRequest(from, address, abi, method, args, isRead);
    }

    /**
     * Handle Base64 encoded byte/string inputs (byte arrays must be base64 encoded to put them on
     * the wire w/ JSON)
     *
     * @param method
     * @param args
     * @return
     * @throws APIException
     */
    private Object[] decodeArgs(ContractABI.Function method, Object[] args) throws APIException {
        if (args == null || args.length == 0) {
            return args;
        }

        List<ContractABI.Entry.Param> params = method.inputs;
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            ContractABI.Entry.Param param = params.get(i);
            if (param.type instanceof SolidityType.Bytes32Type && arg instanceof String) {
                args[i] = new String(Base64.decode((String) arg));
            }
        }

        return args;
    }

    @RequestMapping("/transactions/list")
    public ResponseEntity<APIResponse> listTransactions(
            @JsonMethodArgumentResolver.JsonBodyParam String address) throws APIException {

        List<Transaction> txns = contractService.listTransactions(address);

        List<APIData> data = new ArrayList<>();
        for (Transaction tx : txns) {
            data.add(tx.toAPIData());
        }

        APIResponse res = new APIResponse();
        res.setData(data);

        return new ResponseEntity<>(res, HttpStatus.OK);
    }

  @RequestMapping("/transact")
  public WebAsyncTask<ResponseEntity<APIResponse>> transact(
    @JsonMethodArgumentResolver.JsonBodyParam final String from,
    @JsonMethodArgumentResolver.JsonBodyParam final String address,
    @JsonMethodArgumentResolver.JsonBodyParam final String method,
    @JsonMethodArgumentResolver.JsonBodyParam final Object[] args,
    @JsonMethodArgumentResolver.JsonBodyParam final String privateFrom,
    @JsonMethodArgumentResolver.JsonBodyParam final List<String> privateFor) throws APIException {

    Callable<ResponseEntity<APIResponse>> callable = new Callable<ResponseEntity<APIResponse>>() {
      @Override
      public ResponseEntity<APIResponse> call() throws Exception {
        TransactionRequest req = createTransactionRequest(from, address, method, args, false, null);
        req.setPrivateFrom(privateFrom);
        req.setPrivateFor(privateFor);

        TransactionResult tr = contractService.transact(req);
        APIResponse res = new APIResponse();
        res.setData(tr.toAPIData());
        ResponseEntity<APIResponse> response = new ResponseEntity<>(res, HttpStatus.OK);
        return response;
      }
    };
    WebAsyncTask asyncTask = new WebAsyncTask(callable);
    return asyncTask;
  }


    private APIData toAPIData(Contract c) {
        return new APIData(c.getAddress(), Contract.API_DATA_TYPE, c);
    }

    private List<APIData> toAPIData(List<Contract> contracts) {
        List<APIData> data = new ArrayList<>();
        for (Contract c : contracts) {
           data.add(toAPIData(c));
        }
        return data;
    }

/*
    @RequestMapping("/downloadIPFSFile")
    public WebAsyncTask<ResponseEntity<APIResponse>> ipfsDownload(
            @JsonMethodArgumentResolver.JsonBodyParam final String destPath,
            @JsonMethodArgumentResolver.JsonBodyParam final String docId
    ) throws APIException, IOException {

        Callable<ResponseEntity<APIResponse>> callable = new Callable<ResponseEntity<APIResponse>>() {
            @Override
            public ResponseEntity<APIResponse> call() throws Exception {
                FileOutputStream fos = new FileOutputStream(destPath);
                byte[] fileContents = ipfs.cat(Multihash.fromBase58(docId));
                fos.write(fileContents);
                fos.close();
                APIResponse res = new APIResponse();
                res.setData(destPath);
                ResponseEntity<APIResponse> response = new ResponseEntity<>(res, HttpStatus.OK);
                return response;
            }
        };
        WebAsyncTask asyncTask = new WebAsyncTask(callable);
        return asyncTask;
    }

    @RequestMapping("/embedIPFS")
    public WebAsyncTask<ResponseEntity<APIResponse>> upload(
            @JsonMethodArgumentResolver.JsonBodyParam final String from, //doc uploader
            @JsonMethodArgumentResolver.JsonBodyParam final String address, //contract instance to upload docs
            @JsonMethodArgumentResolver.JsonBodyParam final String hash, // base58 encoded file hash eg: QmPZ9gcCEpqKTo6aq61g2nXGUhM4iCL3ewB6LDXZCtioEB
            @JsonMethodArgumentResolver.JsonBodyParam final String docId, // docId should correspond to the variable name in the sol contract eg: isinGenerationDocId
            @JsonMethodArgumentResolver.JsonBodyParam final String privateFrom,
            @JsonMethodArgumentResolver.JsonBodyParam final List<String> privateFor /*to look into this/) throws APIException {

        final String methodName = docId+"HashUpdate";
        final Object args[] = new Object[] {hash};

        Callable<ResponseEntity<APIResponse>> callable = new Callable<ResponseEntity<APIResponse>>() {
            @Override
            public ResponseEntity<APIResponse> call() throws Exception {
                TransactionRequest req = createTransactionRequest(from, address, methodName, args, false, null);
                req.setPrivateFrom(privateFrom);
                req.setPrivateFor(privateFor);

                TransactionResult tr = contractService.transact(req);
                APIResponse res = new APIResponse();
                res.setData(tr.toAPIData());
                ResponseEntity<APIResponse> response = new ResponseEntity<>(res, HttpStatus.OK);
                return response;
            }
        };
        WebAsyncTask asyncTask = new WebAsyncTask(callable);
        return asyncTask;
    }
*/
}

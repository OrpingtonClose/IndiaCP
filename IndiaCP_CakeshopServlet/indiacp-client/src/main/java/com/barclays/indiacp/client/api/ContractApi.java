package main.java.com.barclays.indiacp.client.api;

import main.java.com.barclays.indiacp.client.ApiClient;
import main.java.com.barclays.indiacp.client.model.Contract;
import main.java.com.barclays.indiacp.client.model.Transaction;
import main.java.com.barclays.indiacp.client.model.TransactionResult;
import main.java.com.barclays.indiacp.client.model.req.ContractCompileCommand;
import main.java.com.barclays.indiacp.client.model.req.ContractCreateCommand;
import main.java.com.barclays.indiacp.client.model.req.ContractMethodCallCommand;
import main.java.com.barclays.indiacp.client.model.res.APIData;
import main.java.com.barclays.indiacp.client.model.res.APIResponse;

import java.util.List;

import javax.inject.Named;

import feign.Headers;
import feign.RequestLine;

public interface ContractApi extends ApiClient.Api {

    @RequestLine("POST /contract/get")
    @Headers({ "Content-type: application/json", "Accepts: application/json" })
    APIResponse<APIData<Contract>, Contract> get(@Named("address") String address);

    @RequestLine("POST /contract/list")
    @Headers({ "Content-type: application/json", "Accepts: application/json" })
    APIResponse<List<APIData<Contract>>, Contract> list();

    @RequestLine("POST /contract/compile")
    @Headers({ "Content-type: application/json", "Accepts: application/json" })
    APIResponse<List<APIData<Contract>>, Contract> compile(ContractCompileCommand command);

    @RequestLine("POST /contract/create")
    @Headers({ "Content-type: application/json", "Accepts: application/json" })
    APIResponse<APIData<TransactionResult>, TransactionResult> create(ContractCreateCommand command);

    @RequestLine("POST /contract/read")
    @Headers({ "Content-type: application/json", "Accepts: application/json" })
    APIResponse<List<Object>, Object> read(ContractMethodCallCommand call);

    @RequestLine("POST /contract/transact")
    @Headers({ "Content-type: application/json", "Accepts: application/json" })
    APIResponse<APIData<TransactionResult>, TransactionResult> transact(ContractMethodCallCommand call);

    @RequestLine("POST /contract/transactions/list")
    @Headers({ "Content-type: application/json", "Accepts: application/json" })
    APIResponse<List<APIData<Transaction>>, Transaction> listTransactions(@Named("address") String address);

}

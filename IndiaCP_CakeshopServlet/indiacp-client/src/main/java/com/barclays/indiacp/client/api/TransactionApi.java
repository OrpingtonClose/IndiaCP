package main.java.com.barclays.indiacp.client.api;

import javax.inject.Named;

import feign.Headers;
import feign.RequestLine;

import main.java.com.barclays.indiacp.client.ApiClient;
import main.java.com.barclays.indiacp.client.model.Transaction;
import main.java.com.barclays.indiacp.client.model.res.APIData;
import main.java.com.barclays.indiacp.client.model.res.APIResponse;

import java.util.List;

public interface TransactionApi extends ApiClient.Api {

    /**
     * Retrieve a transaction by id
     *
     * @param id
     *
     * @return APIResponse<APIData<Transaction>>
     */
    @RequestLine("POST /transaction/get")
    @Headers({ "Content-type: application/json", "Accepts: application/json", })
    APIResponse<APIData<Transaction>, Transaction> get(@Named("id") String id);
    
    @RequestLine("POST /transaction/list")
    @Headers({ "Content-type: application/json", "Accepts: application/json", })
    APIResponse<List<APIData<Transaction>>, Transaction> get(@Named("ids") List<String> ids);

}

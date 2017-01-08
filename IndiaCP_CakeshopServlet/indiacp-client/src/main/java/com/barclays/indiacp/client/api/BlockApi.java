package main.java.com.barclays.indiacp.client.api;

import main.java.com.barclays.indiacp.client.ApiClient;
import main.java.com.barclays.indiacp.client.model.Block;
import main.java.com.barclays.indiacp.client.model.req.BlockGetCommand;
import main.java.com.barclays.indiacp.client.model.res.APIData;
import main.java.com.barclays.indiacp.client.model.res.APIResponse;

import feign.Headers;
import feign.RequestLine;

public interface BlockApi extends ApiClient.Api {

    /**
     * Retrieve a block by id, number or tag
     *
     * @param command
     *
     * @return APIResponse<APIData<Block>>
     */
    @RequestLine("POST /block/get")
    @Headers({ "Content-type: application/json", "Accepts: application/json", })
    APIResponse<APIData<Block>, Block> get(BlockGetCommand command);

}

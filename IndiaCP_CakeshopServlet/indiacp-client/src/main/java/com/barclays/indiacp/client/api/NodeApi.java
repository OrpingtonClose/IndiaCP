package main.java.com.barclays.indiacp.client.api;

import main.java.com.barclays.indiacp.client.ApiClient;
import main.java.com.barclays.indiacp.client.model.Node;
import main.java.com.barclays.indiacp.client.model.req.NodeUpdateCommand;
import main.java.com.barclays.indiacp.client.model.res.APIData;
import main.java.com.barclays.indiacp.client.model.res.APIResponse;
import main.java.com.barclays.indiacp.client.model.res.SimpleResult;

import feign.Headers;
import feign.RequestLine;

public interface NodeApi extends ApiClient.Api {

    /**
     * Get node info and status
     *
     * @return APIResponse<APIData<Node>>
     */
    @RequestLine("POST /node/get")
    @Headers({ "Content-type: application/json", "Accepts: application/json", })
    APIResponse<APIData<Node>, Node> get();

    @RequestLine("POST /node/update")
    @Headers({ "Content-type: application/json", "Accepts: application/json", })
    APIResponse<APIData<Node>, Node> update(NodeUpdateCommand command);

    @RequestLine("POST /node/start")
    @Headers({ "Content-type: application/json", "Accepts: application/json", })
    APIResponse<APIData<SimpleResult>, Boolean> start();

    @RequestLine("POST /node/stop")
    @Headers({ "Content-type: application/json", "Accepts: application/json", })
    APIResponse<APIData<SimpleResult>, Boolean> stop();

    @RequestLine("POST /node/restart")
    @Headers({ "Content-type: application/json", "Accepts: application/json", })
    APIResponse<APIData<SimpleResult>, Boolean> restart();

    @RequestLine("POST /node/reset")
    @Headers({ "Content-type: application/json", "Accepts: application/json", })
    APIResponse<APIData<SimpleResult>, Boolean> reset();

}

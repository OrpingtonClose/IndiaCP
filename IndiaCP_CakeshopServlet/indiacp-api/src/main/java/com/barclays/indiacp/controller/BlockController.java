package com.barclays.indiacp.controller;

import com.barclays.indiacp.config.JsonMethodArgumentResolver;
import com.barclays.indiacp.error.APIException;
import com.barclays.indiacp.model.APIError;
import com.barclays.indiacp.model.APIResponse;
import com.barclays.indiacp.model.Block;
import com.barclays.indiacp.service.BlockService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/block",
    method = RequestMethod.POST,
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE)
public class BlockController extends BaseController {

    @Autowired
    BlockService blockService;

    @RequestMapping("/get")
    public ResponseEntity<APIResponse> getBlock(
            @JsonMethodArgumentResolver.JsonBodyParam(required=false) String id,
            @JsonMethodArgumentResolver.JsonBodyParam(required=false) String hash,
            @JsonMethodArgumentResolver.JsonBodyParam(required=false) Long number,
            @JsonMethodArgumentResolver.JsonBodyParam(required=false) String tag) throws APIException {

        if (id == null && hash != null) {
            id = hash; // backwards compat
        }

        Block block = blockService.get(id, number, tag);

        APIResponse res = new APIResponse();

        if (block != null) {
            res.setData(block.toAPIData());
            return new ResponseEntity<>(res, HttpStatus.OK);
        }

        APIError err = new APIError();
        err.setStatus("404");
        err.setTitle("Block not found");
        res.addError(err);
        return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
    }

}

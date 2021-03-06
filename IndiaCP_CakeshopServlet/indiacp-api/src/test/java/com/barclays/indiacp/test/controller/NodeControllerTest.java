package com.barclays.indiacp.test.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.barclays.indiacp.controller.NodeController;
import com.google.gson.JsonParser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.testng.annotations.Test;


public class NodeControllerTest extends BaseControllerTest {

    @Autowired
    NodeController nodeController;

    private static JsonParser jsonParser;

    public NodeControllerTest() {
        super();
        jsonParser = new JsonParser();
    }

    @Override
    public Object getController() {
    	return nodeController;
    }

    @Test
    public void testInvalidEndPoint() throws Exception {
        mockMvc.perform(post("/api/node/testendpoint")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(""))
            .andExpect(status().isNotFound());
    }

    @Test
    public void testNodeGet() throws Exception {
        mockMvc.perform(post("/api/node/get")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(""))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

}

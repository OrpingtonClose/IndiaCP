package com.barclays.indiacp.test;

import static org.testng.Assert.*;

import com.barclays.indiacp.model.Node;
import com.barclays.indiacp.service.NodeService;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

public class NodeServiceTest extends BaseGethRpcTest {

	@Autowired
	private NodeService nodeService;

	@Test
	public void testGet() throws IOException {
	    Node node = nodeService.get();
	    assertNotNull(node);
	    assertEquals(node.getStatus(), "running");
	}

}

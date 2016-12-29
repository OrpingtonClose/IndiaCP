package com.barclays.indiacp.test;

import static org.testng.Assert.*;

import com.barclays.indiacp.service.GethHttpService;
import com.barclays.indiacp.error.APIException;
import com.barclays.indiacp.service.BlockService;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

public class GethHttpServiceTest extends BaseGethRpcTest {

    @Autowired
    private GethHttpService geth;

    @Autowired
    private BlockService blockService;

    @Test
    public void testReset() throws APIException {
        assertTrue(geth.isRunning());

        String blockId = blockService.get(null, 1L, null).getId();

        assertTrue(geth.reset());
        assertTrue(geth.isRunning());

        assertNotEquals(blockService.get(null, 1L, null).getId(), blockId);
    }

}

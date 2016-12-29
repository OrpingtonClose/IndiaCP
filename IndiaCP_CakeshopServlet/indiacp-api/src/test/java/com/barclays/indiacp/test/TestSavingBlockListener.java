package com.barclays.indiacp.test;

import com.barclays.indiacp.db.SavingBlockListener;
import com.barclays.indiacp.model.Block;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class TestSavingBlockListener extends SavingBlockListener {

    public TestSavingBlockListener() {
    }

    @Override
    public void shutdown() {
    }

    @Override
    public void blockCreated(Block block) {
        saveBlock(block);
    }

}

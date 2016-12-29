package com.barclays.indiacp.db;

import com.barclays.indiacp.model.Block;


public interface BlockListener {

    public void blockCreated(Block block);

    public void shutdown();

}

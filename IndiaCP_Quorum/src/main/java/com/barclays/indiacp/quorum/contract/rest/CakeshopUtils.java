package com.barclays.indiacp.quorum.contract.rest;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import com.jpmorgan.cakeshop.client.ClientManager;
import com.jpmorgan.cakeshop.client.api.ContractApi;
import com.jpmorgan.cakeshop.client.model.Transaction;
import com.jpmorgan.cakeshop.client.model.TransactionResult;
import com.jpmorgan.cakeshop.client.proxy.ContractProxyBuilder;
import com.jpmorgan.cakeshop.client.ws.TransactionEventHandler;
import com.jpmorgan.cakeshop.model.ContractABI;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by ritukedia on 28/12/16.
 */
public class CakeshopUtils {

    public static ContractApi getCakeshopContractApi() {
        // setup cakeshop manager
        final ClientManager manager = ClientManager.create("http://localhost:8080/cakeshop");
        ContractApi contractApi = manager.getClient(ContractApi.class);
        return contractApi;
    }
}

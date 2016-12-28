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

        // watch txn events
//        manager.subscribe(new TransactionEventHandler() {
//            @Override
//            public void onData(Transaction data) {
//                System.out.println("Got txn: " + data.getId());
//            }
//        });

        // incr val by 1000
//        TransactionResult tr = ss2.set(((int) get2.get(0)) + 1000);
//
//        // wait for txn to be committed and print info
//        final ListenableFuture<Transaction> txFuture = manager.waitForTx(tr);
//        txFuture.addListener(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    System.out.println("tx committed:\n" + txFuture.get().toString());
//                    System.out.println(ss2.get()); // read new value
//                    System.out.print("bye!");
//                    manager.shutdown();
//                    System.exit(0);
//                } catch (InterruptedException | ExecutionException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, MoreExecutors.directExecutor());

        return contractApi;
    }
}

package main.java.com.barclays.indiacp.client;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import main.java.com.barclays.indiacp.client.api.BlockApi;
import main.java.com.barclays.indiacp.client.api.ContractApi;
import main.java.com.barclays.indiacp.client.api.NodeApi;
import main.java.com.barclays.indiacp.client.api.TransactionApi;
import main.java.com.barclays.indiacp.client.api.WalletApi;
import main.java.com.barclays.indiacp.client.model.Transaction;
import main.java.com.barclays.indiacp.client.model.TransactionResult;
import main.java.com.barclays.indiacp.client.ws.EventHandler;
import main.java.com.barclays.indiacp.client.ws.TransactionEventHandler;
import main.java.com.barclays.indiacp.client.ws.WebSocketClient;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.util.concurrent.FailureCallback;
import org.springframework.util.concurrent.SuccessCallback;

public class ClientManager {

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(ClientManager.class);

    private final ApiClient apiClient;

    private final WebSocketClient wsClient;

    private final Map<Class<? extends ApiClient.Api>, ApiClient.Api> clients;

    @SuppressWarnings("unchecked")
    private static final Class<? extends ApiClient.Api>[] API_LIST = new Class[] {
            BlockApi.class,
            ContractApi.class,
            NodeApi.class,
            TransactionApi.class,
            WalletApi.class
            };

    public static ClientManager create(String cakeshopBaseUri) {
        // create URIs
        URI cakeshopUri = URI.create(cakeshopBaseUri);
        int port = cakeshopUri.getPort() > 0 ? cakeshopUri.getPort() : 80;
        String scheme = cakeshopUri.getScheme();
        if (StringUtils.isBlank(scheme)) {
            if (port == 443 || port == 8443) {
                scheme = "https";
            } else {
                scheme = "http";
            }
        }

        String baseUri = cakeshopUri.getHost() + ":" + port + cakeshopUri.getPath();
        String apiUri = scheme + "://" + baseUri + "/api";
        String wsUri = (scheme.contentEquals("http") ? "ws" : "wss") + "://" + baseUri + "/ws";

        LOG.debug("Using API URI: " + apiUri);
        LOG.debug("Using WS  URI: " + wsUri);

        return new ClientManager(
                new ApiClient().setBasePath(apiUri),
                new WebSocketClient(wsUri));
    }

    public ClientManager(ApiClient apiClient, WebSocketClient wsClient) {
        this.apiClient = apiClient;
        this.wsClient = wsClient;
        this.clients = new HashMap<>();
        createApiClients();
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void createApiClients() {
        for (Class clazz : API_LIST) {
            this.clients.put(clazz, apiClient.buildClient(clazz));
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends ApiClient.Api> T getClient(Class<T> clientClass) {
        return (T) clients.get(clientClass);
    }

    private void lazyStartWsClient() {
        if (!wsClient.isStarted()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    wsClient.start();
                }
            }).start();
        }
    }

    public void subscribe(EventHandler<?> handler) {
        lazyStartWsClient();
        wsClient.subscribe(handler);
    }

    public void addConnectListener(SuccessCallback<StompSession> listener) {
        wsClient.addConnectListener(listener);
        lazyStartWsClient();
    }

    public void addDisconnectListener(FailureCallback listener) {
        wsClient.addDisconnectListener(listener);
    }

    public void shutdown() {
        if (wsClient != null && !wsClient.isShutdown()) {
            wsClient.shutdown();
        }
    }

    public ListenableFuture<Transaction> waitForTx(TransactionResult txResult) {
        return waitForTx(txResult.getId());
    }

    public ListenableFuture<Transaction> waitForTx(String txId) {
        final SettableFuture<Transaction> future = SettableFuture.create();

        subscribe(new TransactionEventHandler(txId) {
            @Override
            public void onData(Transaction data) {
                future.set(data);
                unsubscribe();
            }
        });

        return future;
    }


}

package io.swagger.api.factories;

import io.swagger.api.TransactionhistoryApiService;
import io.swagger.api.impl.TransactionhistoryApiServiceImpl;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2016-12-21T10:49:10.288Z")
public class TransactionhistoryApiServiceFactory {
    private final static TransactionhistoryApiService service = new TransactionhistoryApiServiceImpl();

    public static TransactionhistoryApiService getTransactionhistoryApi() {
        return service;
    }
}

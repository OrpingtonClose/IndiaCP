package io.swagger.api.factories;

import io.swagger.api.CpissuesApiService;
import io.swagger.api.impl.CpissuesApiServiceImpl;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2016-12-21T10:49:10.288Z")
public class CpissuesApiServiceFactory {
    private final static CpissuesApiService service = new CpissuesApiServiceImpl();

    public static CpissuesApiService getCpissuesApi() {
        return service;
    }
}

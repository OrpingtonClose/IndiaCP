package io.swagger.api.factories;

import io.swagger.api.CpissueApiService;
import io.swagger.api.impl.CpissueApiServiceImpl;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2016-12-21T10:49:10.288Z")
public class CpissueApiServiceFactory {
    private final static CpissueApiService service = new CpissueApiServiceImpl();

    public static CpissueApiService getCpissueApi() {
        return service;
    }
}

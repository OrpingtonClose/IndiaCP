package io.swagger.api.factories;

import io.swagger.api.CpprogramsApiService;
import io.swagger.api.impl.CpprogramsApiServiceImpl;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2016-12-21T10:49:10.288Z")
public class CpprogramsApiServiceFactory {
    private final static CpprogramsApiService service = new CpprogramsApiServiceImpl();

    public static CpprogramsApiService getCpprogramsApi() {
        return service;
    }
}

package io.swagger.api.factories;

import io.swagger.api.CpprogramApiService;
import io.swagger.api.impl.CpprogramApiServiceImpl;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2016-12-21T10:49:10.288Z")
public class CpprogramApiServiceFactory {
    private final static CpprogramApiService service = new CpprogramApiServiceImpl();

    public static CpprogramApiService getCpprogramApi() {
        return service;
    }
}

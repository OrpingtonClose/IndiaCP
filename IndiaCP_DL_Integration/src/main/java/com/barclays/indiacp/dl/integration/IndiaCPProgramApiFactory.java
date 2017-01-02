package com.barclays.indiacp.dl.integration;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import org.glassfish.hk2.api.Factory;

/**
 * Created by ritukedia on 23/12/16.
 */
public class IndiaCPProgramApiFactory implements Factory<IndiaCPProgramApi> {

    private IndiaCPProgramApi proxy;

    @Override
    public IndiaCPProgramApi provide() {
        String resourcePath = IndiaCPProgramApi.class.getAnnotation(javax.ws.rs.Path.class).value();
        InvocationHandler handler = new DLRestProxyHandler(resourcePath);
        ClassLoader classLoader = IndiaCPProgramApi.class.getClassLoader();
        Class[] interfaces = new Class[] { IndiaCPProgramApi.class };
        proxy = (IndiaCPProgramApi) Proxy.newProxyInstance(
                classLoader,
                interfaces,
                handler
        );

        return proxy;
    }

    @Override
    public void dispose(IndiaCPProgramApi indiaCPProgramApi) {
    }
}

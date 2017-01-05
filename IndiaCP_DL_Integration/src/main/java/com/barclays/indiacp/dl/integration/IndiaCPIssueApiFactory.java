package com.barclays.indiacp.dl.integration;

import org.glassfish.hk2.api.Factory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * Created by ritukedia on 23/12/16.
 */
public class IndiaCPIssueApiFactory implements Factory<IndiaCPIssueApi> {

    private IndiaCPIssueApi proxy;

    @Override
    public IndiaCPIssueApi provide() {
        String resourcePath = IndiaCPIssueApi.class.getAnnotation(javax.ws.rs.Path.class).value();
        InvocationHandler handler = new DLRestProxyHandler(resourcePath);
        ClassLoader classLoader = IndiaCPIssueApi.class.getClassLoader();
        Class[] interfaces = new Class[] { IndiaCPIssueApi.class };
        proxy = (IndiaCPIssueApi) Proxy.newProxyInstance(
                classLoader,
                interfaces,
                handler
        );

        return proxy;
    }

    @Override
    public void dispose(IndiaCPIssueApi indiaCPIssueApi) {
    }
}

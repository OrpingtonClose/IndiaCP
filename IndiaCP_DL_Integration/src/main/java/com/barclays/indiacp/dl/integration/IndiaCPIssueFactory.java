package com.barclays.indiacp.dl.integration;

import org.glassfish.hk2.api.Factory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * Created by ritukedia on 23/12/16.
 */
public class IndiaCPIssueFactory implements Factory<IndiaCPIssue> {

    private IndiaCPIssue proxy;

    @Override
    public IndiaCPIssue provide() {
        String resourcePath = IndiaCPIssue.class.getAnnotation(javax.ws.rs.Path.class).value();
        InvocationHandler handler = new DLRestProxyHandler(resourcePath);
        ClassLoader classLoader = IndiaCPIssue.class.getClassLoader();
        Class[] interfaces = new Class[] { IndiaCPIssue.class };
        proxy = (IndiaCPIssue) Proxy.newProxyInstance(
                classLoader,
                interfaces,
                handler
        );

        return proxy;
    }

    @Override
    public void dispose(IndiaCPIssue indiaCPIssue) {
    }
}

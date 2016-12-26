package com.barclays.indiacp.dl.integration;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import org.glassfish.hk2.api.Factory;

/**
 * Created by ritukedia on 23/12/16.
 */
public class IndiaCPProgramFactory implements Factory<IndiaCPProgram> {

    private IndiaCPProgram proxy;

    @Override
    public IndiaCPProgram provide() {
        String resourcePath = IndiaCPProgram.class.getAnnotation(javax.ws.rs.Path.class).value();
        InvocationHandler handler = new DLRestProxyHandler(resourcePath);
        ClassLoader classLoader = IndiaCPProgram.class.getClassLoader();
        Class[] interfaces = new Class[] { IndiaCPProgram.class };
        proxy = (IndiaCPProgram) Proxy.newProxyInstance(
                classLoader,
                interfaces,
                handler
        );

        return proxy;
    }

    @Override
    public void dispose(IndiaCPProgram indiaCPProgram) {
    }
}

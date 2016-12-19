package com.docmanager.cp;

/**
 * Created by Electrania.com on 12/10/2016.
 */
import javax.ws.rs.core.Application;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class DocumentHandler extends Application {
    private Set<Object> singletons = new HashSet<Object>();

    public DocumentHandler() {
        singletons.add(new DocumentRestService());
    }

    @Override
    public Set<Object> getSingletons() {
        return singletons;
    }

    public Set<Class<?>> getClasses() {
        return new HashSet<Class<?>>(Arrays.asList(DocumentRestService.class, Roles.class));
    }
}
package it.sparks.postinstagram.wsrest.utils;

import it.sparks.postinstagram.wsrest.WebServices;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("resources")
public class PackageClasses extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        final Set<Class<?>> classes = new HashSet<Class<?>>();
        classes.add(WebServices.class);
        return classes;
    }

}

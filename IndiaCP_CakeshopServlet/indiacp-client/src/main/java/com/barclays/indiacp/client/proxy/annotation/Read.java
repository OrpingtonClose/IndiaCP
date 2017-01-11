package main.java.com.barclays.indiacp.client.proxy.annotation;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import main.java.com.barclays.indiacp.client.proxy.ValueConstants;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target(METHOD)
@Retention(RUNTIME)
public @interface Read {

    /**
     * Contract method name. Defaults to the name of the annotated method
     * @return
     */
    String value() default ValueConstants.DEFAULT_NONE;

    /**
     * If true, byte outputs (such as bytes32) will be converted into String before returning
     * @return
     */
    boolean treatBytesAsStrings() default false;
}

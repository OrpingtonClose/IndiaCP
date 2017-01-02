package main.java.com.barclays.indiacp.client.proxy.annotation;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import main.java.com.barclays.indiacp.client.proxy.ValueConstants;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target(METHOD)
@Retention(RUNTIME)
public @interface Transact {
    String value() default ValueConstants.DEFAULT_NONE;
}

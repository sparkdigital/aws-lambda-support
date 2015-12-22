package org.jstorni.lambdasupport.endpoint.annotations.apigateway;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({TYPE, METHOD})
@Retention(RUNTIME)
public @interface HttpMethod {

    String actionName();
    
    MethodType httpMethod() default MethodType.GET;
    
    Class<?> payloadClass() default Void.class;
    
    boolean requireInputPayload() default false;
    
    boolean isProtected() default false;
    
    boolean useCallerIdentity() default false;
    
}
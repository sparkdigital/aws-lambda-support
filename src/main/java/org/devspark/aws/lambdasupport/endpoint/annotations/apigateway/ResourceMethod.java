package org.devspark.aws.lambdasupport.endpoint.annotations.apigateway;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({ METHOD })
@Retention(RUNTIME)
public @interface ResourceMethod {

	String actionName();

	MethodType httpMethod() default MethodType.GET;

}

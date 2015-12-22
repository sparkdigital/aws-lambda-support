package org.jstorni.lambdasupport.endpoint;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

import org.jstorni.lambdasupport.endpoint.annotations.apigateway.HttpMethod;
import org.jstorni.lambdasupport.endpoint.annotations.apigateway.Resource;
import org.jstorni.lambdasupport.endpoint.exceptions.InternalException;

public abstract class BaseEndpoint<T> implements Endpoint {

	private Resource rootResource;

	@Override
	public Set<ActionRegistryEntry> getActionRegistryEntries() {
		Set<ActionRegistryEntry> entries = new HashSet<ActionRegistryEntry>();

		rootResource = getClass().getAnnotation(Resource.class);
		if (rootResource == null) {
			throw new InternalException("Invalid resource: "
					+ getClass().getName()
					+ ". Reason: missing @Resource definition");
		}

		Method[] methods = getClass().getMethods();
		for (Method method : methods) {
			HttpMethod httpMethod = method.getAnnotation(HttpMethod.class);
			if (httpMethod != null) {
				entries.add(buildActionRegistryEntryFromMethod(httpMethod,
						method));
			}
		}

		return entries;
	}

	protected Class<?> getPayloadClass(String actionName, HttpMethod httpMethod) {
		Class<?> payloadClass = httpMethod.payloadClass();
		if (payloadClass == Void.class && httpMethod.requireInputPayload()) {
			throw new InternalException("Expected payload for " + actionName);
		} else if (payloadClass == Void.class
				&& !httpMethod.requireInputPayload()) {
			payloadClass = null;
		}

		return payloadClass;
	}

	protected abstract Object[] getInvocationParameters();

	@SuppressWarnings("unchecked")
	protected ActionRegistryEntry buildActionRegistryEntryFromMethod(
			HttpMethod httpMethod, Method method) {
		String actionName = httpMethod.actionName();
		if (actionName == null) {
			actionName = method.getName();
		}

		actionName = rootResource.name() + "." + actionName;

		Class<?> payloadClass = getPayloadClass(actionName, httpMethod);

		Function<Object, ?> handler;

		try {
			handler = (Function<Object, ?>) method.invoke(this,
					getInvocationParameters());
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			throw new InternalException(
					"Error while obtaining handler for action: " + actionName,
					e);
		}

		return new ActionRegistryEntry(actionName, payloadClass, handler);
	}

}

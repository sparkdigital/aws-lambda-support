package org.jstorni.lambdasupport.endpoint;

import java.util.function.Function;

import org.jstorni.lambdasupport.endpoint.annotations.apigateway.HttpMethod;
import org.jstorni.lambdasupport.endpoint.annotations.apigateway.MethodType;
import org.jstorni.lambdasupport.endpoint.annotations.apigateway.Resource;
import org.jstorni.lorm.Repository;

public abstract class BaseRepositoryEndpoint<T> extends BaseEndpoint<T> {

	private Resource rootResource;

	protected abstract Repository<T> getRepository();

	protected Resource getRootResource() {
		return rootResource;
	}

	@Override
	protected Class<?> getPayloadClass(String actionName, HttpMethod httpMethod) {
		Class<?> payloadClass = httpMethod.payloadClass();
		if (payloadClass == Void.class && httpMethod.requireInputPayload()) {
			payloadClass = getRepository().getEntityClass();
		} else if (payloadClass == Void.class
				&& !httpMethod.requireInputPayload()) {
			payloadClass = null;
		}

		return payloadClass;
	}

	@Override
	protected Object[] getInvocationParameters() {
		return new Object[] { getRepository() };
	}

	@HttpMethod(actionName = "findAll", requireInputPayload = false)
	public Function<Object, ?> findAllHandle(Repository<T> repository) {
		return (payload) -> {
			return repository.findAll();
		};
	}

	@Resource(name = "{id}")
	@HttpMethod(actionName = "findById", requireInputPayload = true, payloadClass = Key.class)
	public Function<Object, ?> findByIdHandle(Repository<T> repository) {
		return (payload) -> {
			return repository.findOne(((Key) payload).getId());
		};
	}

	@Resource(name = "{id}")
	@HttpMethod(actionName = "deleteById", httpMethod = MethodType.DELETE, payloadClass = Key.class, requireInputPayload = true)
	public Function<Object, ?> deleteByIdHandle(Repository<T> repository) {
		return (payload) -> {
			repository.deleteById(((Key) payload).getId());
			return null;
		};
	}

	@SuppressWarnings("unchecked")
	@HttpMethod(actionName = "create", httpMethod = MethodType.POST, requireInputPayload = true)
	public Function<Object, ?> createHandle(Repository<T> repository) {
		return (payload) -> {
			return repository.save((T) payload);
		};
	}

	@SuppressWarnings("unchecked")
	@Resource(name = "{id}")
	@HttpMethod(actionName = "update", httpMethod = MethodType.PUT, requireInputPayload = true)
	public Function<Object, ?> updateHandle(Repository<T> repository) {
		return (payload) -> {
			return repository.save((T) payload);
		};
	}

}

package org.jstorni.lambdasupport.endpoint;

import java.util.function.Function;

import org.jstorni.lambdasupport.endpoint.annotations.apigateway.ResourceMethodPayload;
import org.jstorni.lambdasupport.endpoint.annotations.apigateway.ResourceMethod;
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
	protected Class<?> getPayloadClass(String actionName,
			ResourceMethodPayload resourceMethodPayload) {
		if (resourceMethodPayload == null) {
			return null; 
		}

		Class<?> payloadClass = resourceMethodPayload.payloadClass();
		if (payloadClass == Void.class
				&& resourceMethodPayload.inferPayloadFromRepository()) {
			payloadClass = getRepository().getEntityClass();
		}

		return payloadClass;
	}

	@Override
	protected Object[] getInvocationParameters() {
		return new Object[] { getRepository() };
	}

	@ResourceMethod(actionName = "findAll")
	public Function<Object, ?> findAllHandle(Repository<T> repository) {
		return (payload) -> {
			return repository.findAll();
		};
	}

	@Resource(name = "{id}")
	@ResourceMethod(actionName = "findById")
	@ResourceMethodPayload(payloadClass = Key.class)
	public Function<Object, ?> findByIdHandle(Repository<T> repository) {
		return (payload) -> {
			return repository.findOne(((Key) payload).getId());
		};
	}

	@Resource(name = "{id}")
	@ResourceMethod(actionName = "deleteById", httpMethod = MethodType.DELETE)
	@ResourceMethodPayload(payloadClass = Key.class)
	public Function<Object, ?> deleteByIdHandle(Repository<T> repository) {
		return (payload) -> {
			repository.deleteById(((Key) payload).getId());
			return null;
		};
	}

	@SuppressWarnings("unchecked")
	@ResourceMethod(actionName = "create", httpMethod = MethodType.POST)
	@ResourceMethodPayload(inferPayloadFromRepository = true)
	public Function<Object, ?> createHandle(Repository<T> repository) {
		return (payload) -> {
			return repository.save((T) payload);
		};
	}

	@SuppressWarnings("unchecked")
	@Resource(name = "{id}")
	@ResourceMethod(actionName = "update", httpMethod = MethodType.PUT)
	@ResourceMethodPayload(inferPayloadFromRepository = true)
	public Function<Object, ?> updateHandle(Repository<T> repository) {
		return (payload) -> {
			return repository.save((T) payload);
		};
	}

}

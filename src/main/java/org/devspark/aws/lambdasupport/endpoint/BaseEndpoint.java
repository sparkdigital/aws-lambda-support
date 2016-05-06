package org.devspark.aws.lambdasupport.endpoint;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

import org.devspark.aws.lambdasupport.endpoint.annotations.apigateway.Resource;
import org.devspark.aws.lambdasupport.endpoint.annotations.apigateway.ResourceMethod;
import org.devspark.aws.lambdasupport.endpoint.annotations.apigateway.ResourceMethodPayload;
import org.devspark.aws.lambdasupport.endpoint.exceptions.InternalException;
import org.devspark.aws.lorm.EntityManager;
import org.devspark.aws.lorm.Repository;
import org.devspark.aws.lorm.mapping.EntityToItemMapperImpl;
import org.devspark.aws.lorm.mapping.ItemToEntityMapperImpl;

public abstract class BaseEndpoint implements Endpoint {

    private Resource rootResource;

    protected <T> Repository<T> addRepo(Class<T> entityClass) {
        EntityManager entityManager = Configuration.getEntitymanager();
        entityManager.addEntity(entityClass, new EntityToItemMapperImpl<T>(entityClass),
                new ItemToEntityMapperImpl<T>(entityClass, entityManager),
                new EntityToItemMapperImpl<T>(entityClass));

        return entityManager.getRepository(entityClass);
    }

    @Override
    public Set<ActionRegistryEntry> getActionRegistryEntries() {
        Set<ActionRegistryEntry> entries = new HashSet<ActionRegistryEntry>();

        rootResource = getClass().getAnnotation(Resource.class);
        if (rootResource == null) {
            throw new InternalException("Invalid resource: " + getClass().getName()
                    + ". Reason: missing @Resource definition");
        }

        Method[] methods = getClass().getMethods();
        for (Method method : methods) {
            ResourceMethod resourceMethod = method.getAnnotation(ResourceMethod.class);
            if (resourceMethod != null) {
                ResourceMethodPayload resourceMethodPayload = method
                        .getAnnotation(ResourceMethodPayload.class);
                entries.add(buildActionRegistryEntryFromMethod(resourceMethod,
                        resourceMethodPayload, method));
            }
        }

        return entries;
    }

    protected Class<?> getPayloadClass(String actionName,
            ResourceMethodPayload methodPayload) {
        if (methodPayload == null) {
            return null;
        }

        Class<?> payloadClass = methodPayload.payloadClass();
        if (payloadClass == Void.class) {
            throw new InternalException("Expected payload for " + actionName);
        }

        return payloadClass;
    }

    protected abstract Object[] getInvocationParameters();

    @SuppressWarnings("unchecked")
    protected ActionRegistryEntry buildActionRegistryEntryFromMethod(
            ResourceMethod resourceMethod, ResourceMethodPayload methodPayload,
            Method method) {
        String actionName = resourceMethod.actionName();
        if (actionName == null) {
            actionName = method.getName();
        }

        actionName = rootResource.name() + "." + actionName;

        Class<?> payloadClass = getPayloadClass(actionName, methodPayload);

        Function<Object, ?> handler;

        try {
            // TODO dirty hack
            Object[] allParams = getInvocationParameters();
            Object[] invParams = new Object[method.getParameterCount()];

            if (method.getParameterCount() > 0) {
                if (method.getParameterCount() == allParams.length) {
                    invParams = allParams;
                } else if (method.getParameterCount() == 1 && allParams.length > 1) {
                    invParams[0] = allParams[0];
                } else {
                    throw new IllegalArgumentException("Wrong parameter count "
                            + " expected " + method.getParameterCount() + " actual "
                            + allParams.length);
                }
            }

            handler = (Function<Object, ?>) method.invoke(this, invParams);
        } catch (IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            throw new InternalException(
                    "Error while obtaining handler for action: " + actionName, e);
        }

        return new ActionRegistryEntry(actionName, payloadClass, handler);
    }

}

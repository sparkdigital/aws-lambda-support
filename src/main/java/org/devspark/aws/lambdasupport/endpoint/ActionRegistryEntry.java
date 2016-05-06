package org.devspark.aws.lambdasupport.endpoint;

import java.util.function.Function;

public class ActionRegistryEntry {
    private final String action;
    private final Class<?> payloadClass;
    private final Function<Object, ?> handle;

    public ActionRegistryEntry(String action, Class<?> payloadClass,
            Function<Object, ?> handle) {
        super();
        this.action = action;
        this.payloadClass = payloadClass;
        this.handle = handle;
    }

    public String getAction() {
        return action;
    }

    public Class<?> getPayloadClass() {
        return payloadClass;
    }

    public Function<Object, ?> getHandle() {
        return handle;
    }
}

package org.devspark.aws.lambdasupport.endpoint;

import java.util.Set;

public interface Endpoint {

	Set<ActionRegistryEntry> getActionRegistryEntries();
	
}

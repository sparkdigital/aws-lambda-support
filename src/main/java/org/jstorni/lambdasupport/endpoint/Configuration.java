package org.jstorni.lambdasupport.endpoint;


import org.jstorni.lorm.EntityManager;
import org.jstorni.lorm.dynamodb.DynamoDBEntityManager;

public class Configuration {

	private final static EntityManager entityManager;
		
	static {
		entityManager = new DynamoDBEntityManager();
	}
	
	public static EntityManager getEntitymanager() {
		return entityManager;
	}
	
}

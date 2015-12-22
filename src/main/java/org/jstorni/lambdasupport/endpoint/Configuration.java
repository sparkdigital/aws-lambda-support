package org.jstorni.lambdasupport.endpoint;


import org.jstorni.lorm.EntityManager;
import org.jstorni.lorm.dynamodb.DynamoDBEntityManager;

public class Configuration {

	private final static EntityManager entityManager;
	private final static String ACCESS_KEY = "AKIAI4RAQGOFAGPCQEJA";
	private final static String SECRET_KEY = "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";
		
	static {
		entityManager = new DynamoDBEntityManager(null, null, ACCESS_KEY, SECRET_KEY);
	}
	
	public static EntityManager getEntitymanager() {
		return entityManager;
	}
	
}

package org.devspark.aws.lambdasupport.endpoint;

import org.devspark.aws.lorm.EntityManager;
import org.devspark.aws.lorm.dynamodb.DynamoDBEntityManager;

public class Configuration {

    private final static EntityManager entityManager;

    static {
        entityManager = new DynamoDBEntityManager();
    }

    public static EntityManager getEntitymanager() {
        return entityManager;
    }

}

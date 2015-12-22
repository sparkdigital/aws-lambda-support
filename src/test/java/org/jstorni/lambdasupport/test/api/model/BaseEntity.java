package org.jstorni.lambdasupport.test.api.model;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public abstract class BaseEntity {

	@Id
	@GeneratedValue(generator = "org.jstorni.lorm.id.UUIDIdGenerator")
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}

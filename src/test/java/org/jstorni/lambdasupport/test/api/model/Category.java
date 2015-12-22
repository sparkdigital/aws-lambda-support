package org.jstorni.lambdasupport.test.api.model;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Category extends BaseEntity {
	
	@NotNull
	@Size(min=4, max=32)
	private String description;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}

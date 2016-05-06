package org.devspark.aws.lambdasupport.test.api.model;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Reporter extends BaseEntity {

    @NotNull
    @Size(min = 4, max = 16)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}

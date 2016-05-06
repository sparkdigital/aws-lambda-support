package org.devspark.aws.lambdasupport.test.api.model;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Merchant extends BaseEntity {

    @NotNull
    @Size(min = 8, max = 32)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}

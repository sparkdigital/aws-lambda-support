package org.devspark.aws.lambdasupport.test.api.model;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Embeddable
public class Attachment {
    @NotNull
    @Size(max = 256)
    private String location;

    @NotNull
    @Size(max = 512)
    private String description;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}

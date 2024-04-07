package org.grampus.core.annotation.rest.spec;

import org.grampus.core.annotation.rest.GRestGroup;

public class GRestGroupSpec {
    String id;
    String description;

    public GRestGroupSpec(GRestGroup restGroup) {
        this.id = restGroup.id();
        this.description = restGroup.description();
    }

    public GRestGroupSpec(String id, String description) {
        this.id = id;
        this.description = description;
    }

    public GRestGroupSpec(String id) {
        this.id = id;
        this.description = "";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

package org.grampus.core.annotation.rest.spec;

import org.grampus.core.annotation.rest.GRestGroup;

public class GRestGroupSpec {
    String id;

    public GRestGroupSpec(GRestGroup restGroup) {
        this.id = restGroup.id();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

package org.grampus.swagger.model.properties;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class RefProperty extends AbstractProperty implements Property {
    public static final String TYPE = "ref";

    public RefProperty() {
        setType(TYPE);
    }

    public RefProperty(String ref) {
        this();
    }

    public static boolean isType(String type, String format) {
        if (TYPE.equals(type)) {
            return true;
        } else {
            return false;
        }
    }

    public RefProperty asDefault(String ref) {
        return this;
    }

    public RefProperty description(String description) {
        this.setDescription(description);
        return this;
    }

    @Override
    @JsonIgnore
    public String getType() {
        return this.type;
    }

    @Override
    @JsonIgnore
    public void setType(String type) {
        this.type = type;
    }




    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof RefProperty)) {
            return false;
        }
        RefProperty other = (RefProperty) obj;
        return true;
    }
}

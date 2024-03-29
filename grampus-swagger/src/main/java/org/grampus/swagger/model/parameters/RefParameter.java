package org.grampus.swagger.model.parameters;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class RefParameter extends AbstractParameter implements Parameter {


    public static boolean isType(String type, String format) {
        if ("$ref".equals(type)) {
            return true;
        } else {
            return false;
        }
    }

    public RefParameter description(String description) {
        this.setDescription(description);
        return this;
    }

    @Override
    @JsonIgnore
    public boolean getRequired() {
        return required;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        RefParameter other = (RefParameter) obj;
        return true;
    }
}
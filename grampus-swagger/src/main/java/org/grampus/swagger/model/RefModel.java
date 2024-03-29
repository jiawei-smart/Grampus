package org.grampus.swagger.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.grampus.swagger.model.properties.Property;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RefModel implements Model {
    private String description;
    private Map<String, Property> properties;
    private Object example;
    private String title;
    private Class<?> typeClass;

    public RefModel() {
    }

    public RefModel(String ref) {
//        set$ref(ref);
    }

    public RefModel asDefault(String ref) {
//        this.set$ref(RefType.DEFINITION.getInternalPrefix() + ref);
        return this;
    }

    // not allowed in a $ref
    @JsonIgnore
    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public Class<?> getTypeClass() {
        return typeClass;
    }

    @Override
    public void setTypeClass(Class<?> typeClass) {
        this.typeClass = typeClass;
    }

    // not allowed in a $ref
    @JsonIgnore
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @JsonIgnore
    public Map<String, Property> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Property> properties) {
        this.properties = properties;
    }

    @JsonIgnore
    public Object getExample() {
        return example;
    }

    public void setExample(Object example) {
        this.example = example;
    }

    @Override
    public String getReference() {
        return null;
    }

    @Override
    public void setReference(String reference) {

    }

    public Object clone() {
        RefModel cloned = new RefModel();
        cloned.description = this.description;
        cloned.properties = this.properties;
        cloned.example = this.example;
        cloned.title = this.title;
        cloned.typeClass = this.typeClass;
        return cloned;
    }

    @Override
    @JsonIgnore
    public Map<String, Object> getVendorExtensions() {
        return null;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((description == null) ? 0 : description.hashCode());
        result = prime * result
                + ((title == null) ? 0 : title.hashCode());
        result = prime * result
                + ((typeClass == null) ? 0 : typeClass.hashCode());
        result = prime * result + ((example == null) ? 0 : example.hashCode());
        result = prime * result
                + ((properties == null) ? 0 : properties.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        RefModel other = (RefModel) obj;
        if (description == null) {
            if (other.description != null) {
                return false;
            }
        } else if (!description.equals(other.description)) {
            return false;
        }
        if (example == null) {
            if (other.example != null) {
                return false;
            }
        } else if (!example.equals(other.example)) {
            return false;
        }
        if (properties == null) {
            if (other.properties != null) {
                return false;
            }
        } else if (!properties.equals(other.properties)) {
            return false;
        }
        if (title == null) {
            if (other.title != null) {
                return false;
            }
        } else if (!title.equals(other.title)) {
            return false;
        }
        if (typeClass == null) {
            if (other.typeClass != null) {
                return false;
            }
        } else if (!typeClass.equals(other.typeClass)) {
            return false;
        }
        return true;
    }

}
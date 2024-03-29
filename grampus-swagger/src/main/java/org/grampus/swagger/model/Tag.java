package org.grampus.swagger.model;

import java.util.LinkedHashMap;
import java.util.Map;


public class Tag {
    private Map<String, Object> vendorExtensions = new LinkedHashMap<String, Object>();
    private String name;
    private String description;

    public Tag name(String name) {
        setName(name);
        return this;
    }

    public Tag description(String description) {
        setDescription(description);
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public void setVendorExtensions(Map<String, Object> vendorExtensions) {
        this.vendorExtensions = vendorExtensions;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append("Tag {\n");
        b.append("\tname: ").append(getName()).append("\n");
        b.append("\tdescription: ").append(getDescription()).append("\n");
        b.append("\textensions:").append(vendorExtensions.toString());
        b.append("}");
        return b.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((vendorExtensions == null) ? 0 : vendorExtensions.hashCode());
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
        Tag other = (Tag) obj;
        if (description == null) {
            if (other.description != null) {
                return false;
            }
        } else if (!description.equals(other.description)) {
            return false;
        }
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        if (vendorExtensions == null) {
            if (other.vendorExtensions != null) {
                return false;
            }
        } else if (!vendorExtensions.equals(other.vendorExtensions)) {
            return false;
        }
        return true;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {
        private Map<String, Object> vendorExtensions = new LinkedHashMap<String, Object>();
        private String name;
        private String description;

        private Builder() {
        }

        public static Builder newBuilder() {
            return new Builder();
        }

        public Builder withVendorExtensions(Map<String, Object> vendorExtensions) {
            this.vendorExtensions = vendorExtensions;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withDescription(String description) {
            this.description = description;
            return this;
        }

        public Tag build() {
            Tag tag = new Tag();
            tag.setVendorExtensions(vendorExtensions);
            tag.setName(name);
            tag.setDescription(description);
            return tag;
        }
    }
}

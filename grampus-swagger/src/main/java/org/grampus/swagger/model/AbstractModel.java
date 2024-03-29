package org.grampus.swagger.model;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class AbstractModel implements Model {

    private String reference;
    private String title;
    private Class<?> typeClass;
    private Map<String, Object> vendorExtensions = new LinkedHashMap<String, Object>();

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

    
    public Map<String, Object> getVendorExtensions() {
        return vendorExtensions;
    }

    
    public void setVendorExtension(String name, Object value) {
        if (name.startsWith("x-")) {
            vendorExtensions.put(name, value);
        }
    }

    public void setVendorExtensions(Map<String, Object> vendorExtensions) {
        this.vendorExtensions = vendorExtensions;
    }

    public void cloneTo(Object clone) {
        AbstractModel cloned = (AbstractModel) clone;
        cloned.reference = reference;
        cloned.title = title;
        cloned.typeClass = typeClass;
        if (vendorExtensions == null) {
            cloned.vendorExtensions = vendorExtensions;
        } else {
            for (String key: vendorExtensions.keySet()) {
                cloned.setVendorExtension(key, vendorExtensions.get(key));
            }
        }
    }

    public Object clone() {
        return null;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((vendorExtensions == null) ? 0 : vendorExtensions.hashCode());
        result = prime * result
                + ((reference == null) ? 0 : reference.hashCode());
        result = prime * result
                + ((title == null) ? 0 : title.hashCode());
        result = prime * result
                + ((typeClass == null) ? 0 : typeClass.hashCode());
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
        AbstractModel other = (AbstractModel) obj;
        if (vendorExtensions == null) {
            if (other.vendorExtensions != null) {
                return false;
            }
        } else if (!vendorExtensions.equals(other.vendorExtensions)) {
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
        if (reference == null) {
            if (other.reference != null) {
                return false;
            }
        } else if (!reference.equals(other.reference)) {
            return false;
        }

        return true;
    }


    @Override
    public String getReference() {
        return reference;
    }

    @Override
    public void setReference(String reference) {
        this.reference = reference;
    }

}

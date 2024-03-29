package org.grampus.swagger.model.properties;


import java.util.Map;

public interface Property {
    Property title(String title);

    Property description(String description);

    String getType();

    String getFormat();

    String getTitle();

    void setTitle(String title);

    String getDescription();

    void setDescription(String title);

    Boolean getAllowEmptyValue();

    void setAllowEmptyValue(Boolean value);


    String getName();

    void setName(String name);


    boolean getRequired();

    void setRequired(boolean required);


    Object getExample();

    
    void setExample(Object example);

    @Deprecated

    void setExample(String example);

    Boolean getReadOnly();

    void setReadOnly(Boolean readOnly);

    Integer getPosition();

    void setPosition(Integer position);

    void setDefault(String _default);


    String getAccess();


    void setAccess(String access);

    Map<String, Object> getVendorExtensions();

    /**
     * creates a new instance and renames the property to the given name.
     *
     * @return new shallow copy of the property
     */
    Property rename(String newName);
}

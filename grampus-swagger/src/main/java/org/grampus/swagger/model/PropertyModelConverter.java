package org.grampus.swagger.model;

import org.grampus.swagger.model.properties.*;

import java.util.*;

public class PropertyModelConverter {

    public Property modelToProperty(Model model){

        if(model instanceof ModelImpl) {
            ModelImpl m = (ModelImpl) model;
            if (m.getAdditionalProperties() != null){
                MapProperty mapProperty = new MapProperty();
                mapProperty.setType(m.getType());
                mapProperty.setAllowEmptyValue(m.getAllowEmptyValue());
                mapProperty.setDefault((String) m.getDefaultValue());
                mapProperty.setDescription(m.getDescription());
                mapProperty.setExample(m.getExample());
                mapProperty.setFormat(m.getFormat());
                mapProperty.setName(m.getName());
                mapProperty.setTitle(m.getTitle());
                List<String> required = m.getRequired();
                if (required != null) {
                    for (String name : required) {
                        if (m.getName().equals(name)) {
                            mapProperty.setRequired(true);
                        }
                    }
                }
                mapProperty.setVendorExtensions(m.getVendorExtensions());
                mapProperty.setAdditionalProperties(m.getAdditionalProperties());
                return mapProperty;
            }

            Property property = propertyByType(m);

            if(property instanceof ObjectProperty){
                ObjectProperty objectProperty = (ObjectProperty) property;
                objectProperty.setProperties(model.getProperties());
                objectProperty.setExample(model.getExample());
                return objectProperty;
            }

            return property;

        } else if(model instanceof ArrayModel) {
            ArrayModel m = (ArrayModel) model;
            ArrayProperty property = new ArrayProperty();
            Property inner = m.getItems();
            property.setItems(inner);
            property.setExample(m.getExample());
            property.setMaxItems(m.getMaxItems());
            property.setMinItems(m.getMinItems());
            property.setDescription(m.getDescription());
            property.setTitle(m.getTitle());
            property.setUniqueItems(m.getUniqueItems());
            return property;

        }
        return null;
    }

    private Property propertyByType(ModelImpl model) {
        return PropertyBuilder.build(model.getType(), model.getFormat(), argsFromModel(model));
    }

    private Map<PropertyBuilder.PropertyId, Object> argsFromModel(ModelImpl model) {
        if (model == null) return Collections.emptyMap();
        final Map<PropertyBuilder.PropertyId, Object> args = new EnumMap<>(PropertyBuilder.PropertyId.class);
        args.put(PropertyBuilder.PropertyId.DESCRIPTION, model.getDescription());
        args.put(PropertyBuilder.PropertyId.EXAMPLE, model.getExample());
        args.put(PropertyBuilder.PropertyId.ENUM, model.getEnum());
        args.put(PropertyBuilder.PropertyId.TITLE, model.getTitle());
        args.put(PropertyBuilder.PropertyId.DEFAULT, model.getDefaultValue());
        args.put(PropertyBuilder.PropertyId.DESCRIMINATOR, model.getDiscriminator());
        args.put(PropertyBuilder.PropertyId.MINIMUM, model.getMinimum());
        args.put(PropertyBuilder.PropertyId.MAXIMUM, model.getMaximum());
        args.put(PropertyBuilder.PropertyId.UNIQUE_ITEMS, model.getUniqueItems());
        args.put(PropertyBuilder.PropertyId.VENDOR_EXTENSIONS, model.getVendorExtensions());
        return args;
    }




    public Model propertyToModel(Property property){

        String description = property.getDescription();
        String type = property.getType();
        String format = property.getFormat();
        String example = null;
        String title = property.getTitle();

        /*Object obj = property.getExample();
        if (obj != null) {
            example = obj.toString();
        }*/

        Boolean allowEmptyValue = property.getAllowEmptyValue();

        Map<String, Object> extensions = property.getVendorExtensions();

        Property additionalProperties = null;

        if (property instanceof MapProperty) {
             additionalProperties = ((MapProperty) property).getAdditionalProperties();
        }

        Map<String, Property> properties = null;

        if (property instanceof ObjectProperty) {
            ObjectProperty objectProperty = (ObjectProperty) property;
            properties = objectProperty.getProperties();
        }

        if (property instanceof ArrayProperty){
            ArrayProperty arrayProperty = (ArrayProperty) property;
            ArrayModel arrayModel = new ArrayModel();
            arrayModel.setItems(arrayProperty.getItems());
            arrayModel.setDescription(description);
            arrayModel.setExample(example);
            arrayModel.setUniqueItems(arrayProperty.getUniqueItems());

            if(extensions != null) {
                arrayModel.setVendorExtensions(extensions);
            }

            if (properties != null) {
                arrayModel.setProperties(properties);
            }

            return arrayModel;
        }

        ModelImpl model = new ModelImpl();

        model.setDescription(description);
        model.setExample(property.getExample());//example
        model.setTitle(title);
        model.setType(type);
        model.setFormat(format);
        model.setAllowEmptyValue(allowEmptyValue);

        if(extensions != null) {
            model.setVendorExtensions(extensions);
        }

        if(additionalProperties != null) {
            model.setAdditionalProperties(additionalProperties);
        }

        if (properties != null) {
            model.setProperties(properties);
        }


        return model;
    }
}

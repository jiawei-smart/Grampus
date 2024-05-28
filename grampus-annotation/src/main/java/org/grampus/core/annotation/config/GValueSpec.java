package org.grampus.core.annotation.config;

import org.grampus.log.GLogger;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class GValueSpec {

    private static String PATTERN_KEY = "^([a-zA-Z0-9]+)\\.(.+)$";
    private final Pattern pattern;

    private final Map<String, Object> properties = new HashMap<>();

    public GValueSpec(Map<String, Object> properties) {
        if (properties != null) {
            this.properties.putAll(properties);
        }
        pattern = Pattern.compile(PATTERN_KEY);

    }

    public void enrich(Collection objects){
        for (Object object : objects) {
            enrich(object);
        }
    }

    public void enrich(Object target)  {
        Class<?> clazz = target.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(GValue.class)) {
                GValue annotation = field.getAnnotation(GValue.class);
                String configKey = annotation.value();
                if (properties.containsKey(configKey)) {
                   setValue(field,target,properties.get(configKey));
                }else {
                    if(!setValueBySecondKey(configKey,field, target, properties)){
                        GLogger.error("ConfigPropertySpec.enrich: key '{}' not found", configKey);
                    }else {
                        continue;
                    }
                }
            }else if(field.isAnnotationPresent(GEnvValue.class)){
                GEnvValue annotation = field.getAnnotation(GEnvValue.class);
                String configKey = annotation.value();
                if (System.getProperties().containsKey(configKey)) {
                    setValue(field,target,System.getProperties().get(configKey));
                    continue;
                }else if(System.getenv().containsKey(configKey)) {
                    setValue(field,target,System.getenv().get(configKey));
                    continue;
                }

//                if(!setValueBySecondKey(configKey,field, target, System.getProperties())
//                        && !setValueBySecondKey(configKey,field, target, System.getenv())){
//
//                }
                GLogger.error("ConfigPropertySpec.enrich: key '{}' not found", configKey);
            }
        }
    }

    private boolean setValueBySecondKey(String configKey, Field field, Object target, Properties properties) {
        Matcher matcher = pattern.matcher(configKey);
        if (matcher.matches()) {
            String key = matcher.group(1);
            String remainKey = matcher.group(2);
            if(key != null && properties.containsKey(key)
                    && remainKey != null && properties.get(key)!= null && ((Map)properties.get(key)).containsKey(remainKey)){
                setValue(field,target,((Map)properties.get(key)).get(remainKey));
                return true;
            }
        }
        return false;
    }

    private boolean setValueBySecondKey(String configKey, Field field, Object target, Map properties) {
        Matcher matcher = pattern.matcher(configKey);
        if (matcher.matches()) {
            String key = matcher.group(1);
            String remainKey = matcher.group(2);
            if(key != null && properties.containsKey(key)
                    && remainKey != null && properties.get(key)!= null && ((Map)properties.get(key)).containsKey(remainKey)){
                setValue(field,target,((Map)properties.get(key)).get(remainKey));
                return true;
            }
        }
        return false;
    }

    private void setValue(Field field, Object target, Object value) {
        field.setAccessible(true);
        try {
            if(value.getClass() == field.getType()){
                field.set(target, value);
            }else {
                field.set(target, convertValue(field.getType(), String.valueOf(value)));
            }
        } catch (IllegalAccessException e) {
            GLogger.error("Error in config property [{}], with {}" ,field.getName(), e);
        }
    }

    public String extractKey(String input, Pattern pattern) {
        Matcher matcher = pattern.matcher(input);
        if (matcher.matches()) {
            return matcher.group(1);
        } else {
            return null;
        }
    }

    private Object convertValue(Class<?> targetType, String value) {
        if (targetType == int.class || targetType == Integer.class) {
            return Integer.parseInt(value);
        } else if (targetType == long.class || targetType == Long.class) {
            return Long.parseLong(value);
        } else if (targetType == boolean.class || targetType == Boolean.class) {
            return Boolean.parseBoolean(value);
        } else if (targetType == double.class || targetType == Double.class) {
            return Double.parseDouble(value);
        } else if (targetType == String.class) {
            return value;
        }
        throw new IllegalArgumentException("Unsupported field type: " + targetType);
    }
}

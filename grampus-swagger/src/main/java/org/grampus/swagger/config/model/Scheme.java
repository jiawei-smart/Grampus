package org.grampus.swagger.config.model;

public enum Scheme {
    HTTP("http"),
    HTTPS("https");

    private final String value;

    private Scheme(String value) {
        this.value = value;
    }

    public static Scheme forValue(String value) {
        for (Scheme item : Scheme.values()) {
            if (item.name().equalsIgnoreCase(value) || item.toValue().equalsIgnoreCase(value)) {
                return item;
            }
        }
        return null;
    }

    public String toValue() {
        return value;
    }
}
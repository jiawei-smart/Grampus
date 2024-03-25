package org.grampus.core.annotation.rest.spec;

import org.grampus.core.annotation.rest.GRestBody;
import org.grampus.core.annotation.rest.GRestParam;

public class GRestParamSpec {
    String name;
    String description;
    String example;
    boolean require;

    boolean isBody = false;

    Class type;

    public GRestParamSpec(GRestParam gRestParam, Class type) {
        this.name = gRestParam.name();
        this.description = gRestParam.description();
        this.example = gRestParam.description();
        this.require = gRestParam.require();
        this.type = type;
    }

    public GRestParamSpec(GRestBody restBody, Class paramType) {
        this.description = restBody.description();
        this.example = restBody.example();
        this.isBody = true;
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

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public boolean isRequire() {
        return require;
    }

    public void setRequire(boolean require) {
        this.require = require;
    }

    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }
}

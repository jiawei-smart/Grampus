package org.grampus.swagger.endpoint;

import org.grampus.swagger.spec.EndpointSpec;
import org.grampus.swagger.spec.MethodSpec;
import org.grampus.swagger.GSwagger;
import spark.Filter;
import spark.Route;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ApiEndpoint {

    private final GSwagger swagger;
    private final EndpointSpec endpointSpec;
    private final List<MethodSpec> methodSpecs;

    public ApiEndpoint(final GSwagger swagger, final EndpointSpec endpointSpec) {
        this.swagger = swagger;
        this.endpointSpec = endpointSpec;
        this.endpointSpec.setNameSpace(swagger.getApiPath() + endpointSpec.getPath());
        this.methodSpecs = new ArrayList<>();
    }

    public static ApiEndpoint of(final GSwagger swagger, final EndpointSpec endpointSpec) {
        return new ApiEndpoint(swagger, endpointSpec);
    }

    public List<MethodSpec> getMethodDescriptors() {
        return methodSpecs;
    }

    public EndpointSpec getEndpointDescriptor() {
        return endpointSpec;
    }

    public ApiEndpoint get(MethodSpec methodSpec, Route route) {
        Optional.ofNullable(methodSpec).orElseThrow(() -> new IllegalArgumentException("methodSpec is required"));
        methodSpecs.add(methodSpec);
        swagger.getSpark().get(swagger.getApiPath() + methodSpec.getPath(), route);
        return this;
    }

    public ApiEndpoint post(MethodSpec methodSpec, Route route) {
        Optional.ofNullable(methodSpec).orElseThrow(() -> new IllegalArgumentException("methodSpec is required"));
        methodSpecs.add(methodSpec);
        swagger.getSpark().post(swagger.getApiPath() + methodSpec.getPath(), route);
        return this;
    }

    public ApiEndpoint put(MethodSpec methodSpec, Route route) {
        Optional.ofNullable(methodSpec).orElseThrow(() -> new IllegalArgumentException("methodSpec is required"));
        methodSpecs.add(methodSpec);
        swagger.getSpark().put(swagger.getApiPath() + methodSpec.getPath(), route);
        return this;
    }

    public ApiEndpoint delete(MethodSpec methodSpec, Route route) {
        Optional.ofNullable(methodSpec).orElseThrow(() -> new IllegalArgumentException("methodSpec is required"));
        methodSpecs.add(methodSpec);
        swagger.getSpark().delete(swagger.getApiPath() + methodSpec.getPath(), route);
        return this;
    }

    public ApiEndpoint get(MethodSpec methodSpec, String acceptType, Route route) {
        Optional.ofNullable(methodSpec).orElseThrow(() -> new IllegalArgumentException("methodSpec is required"));
        methodSpecs.add(methodSpec);
        swagger.getSpark().get(swagger.getApiPath() + methodSpec.getPath(), acceptType, route);
        return this;
    }

    public ApiEndpoint post(MethodSpec methodSpec, String acceptType, Route route) {
        Optional.ofNullable(methodSpec).orElseThrow(() -> new IllegalArgumentException("methodSpec is required"));
        methodSpecs.add(methodSpec);
        swagger.getSpark().post(swagger.getApiPath() + methodSpec.getPath(), acceptType, route);
        return this;
    }

    public ApiEndpoint put(MethodSpec methodSpec, String acceptType, Route route) {
        Optional.ofNullable(methodSpec).orElseThrow(() -> new IllegalArgumentException("methodSpec is required"));
        methodSpecs.add(methodSpec);
        swagger.getSpark().put(swagger.getApiPath() + methodSpec.getPath(), acceptType, route);
        return this;
    }

    public ApiEndpoint delete(MethodSpec methodSpec, String acceptType, Route route) {
        Optional.ofNullable(methodSpec).orElseThrow(() -> new IllegalArgumentException("methodSpec is required"));
        methodSpecs.add(methodSpec);
        swagger.getSpark().delete(swagger.getApiPath() + methodSpec.getPath(), acceptType, route);
        return this;
    }

    public ApiEndpoint before(Filter filter) {
        swagger.getSpark().before(filter);
        return this;
    }

    public ApiEndpoint after(Filter filter) {
        swagger.getSpark().after(filter);
        return this;
    }
}

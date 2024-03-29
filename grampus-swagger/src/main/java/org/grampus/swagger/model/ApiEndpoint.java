package org.grampus.swagger.model;

import org.grampus.swagger.descriptor.EndpointDescriptor;
import org.grampus.swagger.descriptor.MethodDescriptor;
import org.grampus.swagger.GSparkSwagger;
import spark.Filter;
import spark.Route;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ApiEndpoint {

    private final GSparkSwagger swagger;
    private final EndpointDescriptor endpointDescriptor;
    private final List<MethodDescriptor> methodDescriptors;

    public ApiEndpoint(final GSparkSwagger swagger, final EndpointDescriptor endpointDescriptor) {
        this.swagger = swagger;
        this.endpointDescriptor = endpointDescriptor;
        this.endpointDescriptor.setNameSpace(swagger.getApiPath() + endpointDescriptor.getPath());
        this.methodDescriptors = new ArrayList<>();
    }

    public static ApiEndpoint of(final GSparkSwagger swagger, final EndpointDescriptor endpointDescriptor) {
        return new ApiEndpoint(swagger, endpointDescriptor);
    }

    public List<MethodDescriptor> getMethodDescriptors() {
        return methodDescriptors;
    }

    public EndpointDescriptor getEndpointDescriptor() {
        return endpointDescriptor;
    }

    public ApiEndpoint get(MethodDescriptor methodSpec, Route route) {
        Optional.ofNullable(methodSpec).orElseThrow(() -> new IllegalArgumentException("methodSpec is required"));
        methodDescriptors.add(methodSpec);
        swagger.getSpark().get(swagger.getApiPath() + methodSpec.getPath(), route);
        return this;
    }

    public ApiEndpoint post(MethodDescriptor methodSpec, Route route) {
        Optional.ofNullable(methodSpec).orElseThrow(() -> new IllegalArgumentException("methodSpec is required"));
        methodDescriptors.add(methodSpec);
        swagger.getSpark().post(swagger.getApiPath() + methodSpec.getPath(), route);
        return this;
    }

    public ApiEndpoint put(MethodDescriptor methodSpec, Route route) {
        Optional.ofNullable(methodSpec).orElseThrow(() -> new IllegalArgumentException("methodSpec is required"));
        methodDescriptors.add(methodSpec);
        swagger.getSpark().put(swagger.getApiPath() + methodSpec.getPath(), route);
        return this;
    }

    public ApiEndpoint delete(MethodDescriptor methodSpec, Route route) {
        Optional.ofNullable(methodSpec).orElseThrow(() -> new IllegalArgumentException("methodSpec is required"));
        methodDescriptors.add(methodSpec);
        swagger.getSpark().delete(swagger.getApiPath() + methodSpec.getPath(), route);
        return this;
    }

    public ApiEndpoint get(MethodDescriptor methodSpec, String acceptType, Route route) {
        Optional.ofNullable(methodSpec).orElseThrow(() -> new IllegalArgumentException("methodSpec is required"));
        methodDescriptors.add(methodSpec);
        swagger.getSpark().get(swagger.getApiPath() + methodSpec.getPath(), acceptType, route);
        return this;
    }

    public ApiEndpoint post(MethodDescriptor methodSpec, String acceptType, Route route) {
        Optional.ofNullable(methodSpec).orElseThrow(() -> new IllegalArgumentException("methodSpec is required"));
        methodDescriptors.add(methodSpec);
        swagger.getSpark().post(swagger.getApiPath() + methodSpec.getPath(), acceptType, route);
        return this;
    }

    public ApiEndpoint put(MethodDescriptor methodSpec, String acceptType, Route route) {
        Optional.ofNullable(methodSpec).orElseThrow(() -> new IllegalArgumentException("methodSpec is required"));
        methodDescriptors.add(methodSpec);
        swagger.getSpark().put(swagger.getApiPath() + methodSpec.getPath(), acceptType, route);
        return this;
    }

    public ApiEndpoint delete(MethodDescriptor methodSpec, String acceptType, Route route) {
        Optional.ofNullable(methodSpec).orElseThrow(() -> new IllegalArgumentException("methodSpec is required"));
        methodDescriptors.add(methodSpec);
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

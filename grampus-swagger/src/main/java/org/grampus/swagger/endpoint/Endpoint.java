package org.grampus.swagger.endpoint;

import org.grampus.swagger.GSwagger;

@FunctionalInterface
public interface Endpoint {
    void bind(final GSwagger restApi);
}
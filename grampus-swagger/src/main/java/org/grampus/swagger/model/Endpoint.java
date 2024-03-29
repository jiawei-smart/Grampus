package org.grampus.swagger.model;

import org.grampus.swagger.GSparkSwagger;

@FunctionalInterface
public interface Endpoint {

    void bind(final GSparkSwagger restApi);
}
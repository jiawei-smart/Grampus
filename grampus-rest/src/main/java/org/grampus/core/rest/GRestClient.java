package org.grampus.core.rest;

import org.grampus.core.client.GClient;
import org.grampus.log.GLogger;
import org.grampus.swagger.GSparkSwagger;
import org.grampus.swagger.model.Endpoint;
import spark.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GRestClient implements GClient<GRestOptions> {
    private List<Endpoint> endpointList = new ArrayList<>();
    private GRestOptions config;

    @Override
    public boolean start(GRestOptions config) {
        this.config =  config;
        try {
            Service spark = Service.ignite()
                    .ipAddress(config.getHost())
                    .port(config.getPort());
            GSparkSwagger.of(spark, config.getSwaggerOptions())
                    .endpoints(() -> endpointList)
                    .generateDoc();
            return true;
        } catch (IOException e) {
            GLogger.error("GRest: failure to start rest client, {}",e);
            return false;
        }
    }

    @Override
    public boolean stop() {
        return true;
    }

    public void registerEndpoint(Endpoint endpoint) {
        this.endpointList.add(endpoint);
    }
}

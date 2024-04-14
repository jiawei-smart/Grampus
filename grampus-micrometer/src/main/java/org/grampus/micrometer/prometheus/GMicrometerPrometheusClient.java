package org.grampus.micrometer.prometheus;

import com.sun.net.httpserver.HttpServer;
import io.github.mweirauch.micrometer.jvm.extras.ProcessMemoryMetrics;
import io.github.mweirauch.micrometer.jvm.extras.ProcessThreadMetrics;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.binder.jvm.*;
import io.micrometer.core.instrument.binder.logging.LogbackMetrics;
import io.micrometer.core.instrument.binder.system.ProcessorMetrics;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.micrometer.prometheus.PrometheusRenameFilter;
import org.grampus.core.client.GAPIClient;
import org.grampus.log.GLogger;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class GMicrometerPrometheusClient implements GAPIClient<GMicrometerPrometheusOptions> {

    @Override
    public boolean start(GMicrometerPrometheusOptions config) {
        PrometheusMeterRegistry registry = new PrometheusMeterRegistry(config.getPrometheusConfig());
        registry.config().meterFilter(new PrometheusRenameFilter());
        registry.config().commonTags("application", config.getName());
        registry.config().commonTags("instance", config.getHostname());
        new ClassLoaderMetrics().bindTo(registry);
        new JvmMemoryMetrics().bindTo(registry);
        new JvmGcMetrics().bindTo(registry);
        new ProcessorMetrics().bindTo(registry);
        new JvmThreadMetrics().bindTo(registry);
        new JvmInfoMetrics().bindTo(registry);
        new LogbackMetrics().bindTo(registry);
        new JvmHeapPressureMetrics().bindTo(registry);
        new ProcessMemoryMetrics().bindTo(registry);
        new ProcessThreadMetrics().bindTo(registry);
        Metrics.addRegistry(registry);
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(config.getPort()), 0);
            server.createContext("/prometheus", httpExchange -> {
                String response = registry.scrape();
                httpExchange.sendResponseHeaders(200, response.getBytes().length);
                try (OutputStream os = httpExchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            });
            new Thread(server::start).start();
        } catch (IOException e) {
            GLogger.error("failure to start the GMicrometer with {}",e);
        }
        return true;
    }

    @Override
    public boolean stop() {
        return false;
    }
}

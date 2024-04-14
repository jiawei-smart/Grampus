package org.grampus.micrometer.prometheus;

import io.micrometer.prometheus.PrometheusConfig;
import org.grampus.core.client.GAPIConfig;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class GMicrometerPrometheusOptions implements GAPIConfig {

    private PrometheusConfig prometheusConfig = PrometheusConfig.DEFAULT;
    public static final int DEFAULT_PORT = 8080;
    private int port = DEFAULT_PORT;
    private String name;

    public PrometheusConfig getPrometheusConfig() {
        return prometheusConfig;
    }

    public void setPrometheusConfig(PrometheusConfig prometheusConfig) {
        this.prometheusConfig = prometheusConfig;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHostname() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            return "localhost";
        }
    }
}

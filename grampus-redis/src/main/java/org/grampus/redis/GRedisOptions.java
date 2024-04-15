package org.grampus.redis;

import org.grampus.core.client.GAPIConfig;

import java.util.List;

public class GRedisOptions implements GAPIConfig {

    private String host;
    private Integer port;

    private List<String> clusterNodes;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public List<String> getClusterNodes() {
        return clusterNodes;
    }

    public void setClusterNodes(List<String> clusterNodes) {
        this.clusterNodes = clusterNodes;
    }
}

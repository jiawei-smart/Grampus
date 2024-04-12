package org.grampus.grpc;

import org.grampus.core.client.GAPIConfig;
import org.grampus.util.GStringUtil;

public class GRpcOptions implements GAPIConfig {
    private int channelPoolSize = 1;
    private String clientHost;
    private Integer clientPort;
    private Integer listenPort;

    public int getChannelPoolSize() {
        return channelPoolSize;
    }

    public void setChannelPoolSize(int channelPoolSize) {
        this.channelPoolSize = channelPoolSize;
    }

    public String getClientHost() {
        return clientHost;
    }

    public void setClientHost(String clientHost) {
        this.clientHost = clientHost;
    }

    public Integer getClientPort() {
        return clientPort;
    }

    public void setClientPort(Integer clientPort) {
        this.clientPort = clientPort;
    }

    public boolean isConfigClient() {
        return GStringUtil.isNotEmpty(this.clientHost) && this.clientPort != null;
    }

    public boolean isConfigServer() {
        return listenPort != null;
    }

    public Integer getListenPort() {
        return listenPort;
    }

    public void setListenPort(Integer listenPort) {
        this.listenPort = listenPort;
    }
}

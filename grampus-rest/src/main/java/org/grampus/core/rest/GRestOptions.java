package org.grampus.core.rest;

import org.grampus.core.client.GAPIConfig;
import org.grampus.swagger.GSwaggerOptions;
import org.grampus.util.GStringUtil;

public class GRestOptions implements GAPIConfig {
    private String host;
    private Integer port;

    private Long initMaxWaitTimeMills = 1000l;

    private GSwaggerOptions swaggerOptions;

    public String getHost() {
        if(GStringUtil.equals(host,"localhost")){
//            try {
//                InetAddress inetadd = InetAddress.getLocalHost();
//                return inetadd.getHostName();
//            } catch (UnknownHostException e) {
//                throw new RuntimeException(e);
//            }
        }
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

    public GSwaggerOptions getSwaggerOptions() {
        if(swaggerOptions == null){
            swaggerOptions = GSwaggerOptions.defaultOptions();
        }
        swaggerOptions.setHost(getHost());
        swaggerOptions.setPort(getPort());
        return swaggerOptions;
    }

    public void setSwaggerOptions(GSwaggerOptions swaggerOptions) {
        this.swaggerOptions = swaggerOptions;
    }

    public Long getInitMaxWaitTimeMills() {
        return initMaxWaitTimeMills;
    }

    public void setInitMaxWaitTimeMills(Long initMaxWaitTimeMills) {
        this.initMaxWaitTimeMills = initMaxWaitTimeMills;
    }
}

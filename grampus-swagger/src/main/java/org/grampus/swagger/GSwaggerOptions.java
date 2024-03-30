package org.grampus.swagger;


import org.grampus.swagger.config.model.Contact;
import org.grampus.util.GStringUtil;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;

public class GSwaggerOptions {
    private String basePath = "";

    private String host;
    private Integer port;
    private String version = "1.0.0";

    private String title = "org.grampus.sample";

    private Contact contact = new Contact().name("org.grampus.sample").email("org.grampus@github.com").url("https://org.grampus");
    private String description;
    private boolean isEnableStaticMapping = true;
    private boolean isEnableCors = false;



    public static GSwaggerOptions defaultOptions() {
        return new GSwaggerOptions();
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public boolean isEnableStaticMapping() {
        return isEnableStaticMapping;
    }

    public boolean isEnableCors() {
        return isEnableCors;
    }

    public static String resolvePublicIp() {
        try {
            return Collections.list(NetworkInterface.getNetworkInterfaces())
                    .stream()
                    .filter(networkInterface -> networkInterface.getName() != null && (networkInterface.getName().equals("eth0") || networkInterface.getName().equals("wlan0")))
                    .flatMap(networkInterface -> Collections.list(networkInterface.getInetAddresses()).stream())
                    .findFirst()
                    .map(InetAddress::getHostAddress)
                    .orElseGet(() -> "localhost");
        } catch (Exception e) {
            return "localhost";
        }
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setEnableStaticMapping(boolean enableStaticMapping) {
        isEnableStaticMapping = enableStaticMapping;
    }

    public void setEnableCors(boolean enableCors) {
        isEnableCors = enableCors;
    }

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

}

package org.grampus.core.environment;

import org.grampus.core.GConstant;
import org.grampus.core.GContext;
import org.grampus.util.GStringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DefaultGEnvironment implements GEnvironment{
    private static Logger log = LoggerFactory.getLogger(DefaultGEnvironment.class.getName());
    private String configYml;
    private List<String> servicesStem;

    @Override
    public String getConfigYaml() {
        configYml = System.getProperty(GConstant.GRAMPUS_CONFIG_YAML);
        if(configYml == null){
            configYml = GConstant.DEFAULT_CONFIG_YAML;
        }
        return configYml;
    }

    public List<String> getServicesNameStem() {
        if(servicesStem == null){
            setServicesStem(System.getProperty(GConstant.GRAMPUS_SERVICES));
        }
        return servicesStem;
    }

    @Override
    public GContext createContext() {
        return new GContext(getConfigYaml());
    }

    public void setServicesStem(String servicesStr) {
       if(GStringUtil.isNotEmpty(servicesStr)){
           this.servicesStem = Arrays.asList(servicesStr.split(",")).stream().map(String::trim).collect(Collectors.toList());
       }else {
           log.warn("services stem is empty!");
           throw new RuntimeException("services stem is empty!");
       }
    }
}

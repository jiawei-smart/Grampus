package org.grampus.core;

import org.grampus.core.environment.DefaultGEnvironment;
import org.grampus.core.environment.DefaultTestEnvironment;
import org.grampus.core.environment.GEnvironment;
import org.grampus.util.GStringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class GWorkflow {
    private static Logger log = LoggerFactory.getLogger(GWorkflow.class.getName());
    private Map<String, GService> services = new HashMap<>();
    private List<String> chains = new ArrayList<>();
    private GContext context;

    public GWorkflow() {
    }

    public GService service(String serviceName, GService service) {
        if (services.containsKey(serviceName)) {
            log.error("Service [{}] already exists", serviceName);
            return null;
        } else {
            service.setName(serviceName);
            service.setContext(this.context);
            this.services.put(serviceName, service);
            return service;
        }
    }

    public GService service(String serviceName) {
        if (services.containsKey(serviceName)) {
            return services.get(serviceName);
        } else {
            return this.service(serviceName, new GService(serviceName));
        }
    }

    public GWorkflow chain(String... chains) {
        if (chains != null && chains.length > 0) {
            for (String chain : chains) {
                this.chains.add(chain);
            }
        }
        return this;
    }

    public String link(String chainStartPoint, String chainEndPoint) {
        return chainStartPoint + GConstant.CHAIN_SPLIT_CHAR + chainEndPoint;
    }

    public String event(String serviceName, String event) {
        return serviceName + "." + event;
    }

    public void startAll() {
        System.setProperty(GConstant.GRAMPUS_SERVICES, GConstant.ALL_SERVICES);
        start();
    }

    public void start() {
        start(new DefaultGEnvironment());
    }

    public void start(GEnvironment environment) {
        this.context = environment.createContext();
        buildWorkflow();
        parseConfigService();
        List<String> serviceNameList = environment.getServicesNameStem();
        if (serviceNameList != null && serviceNameList.size() > 0) {
            if (serviceNameList.size() == 1 && GStringUtil.equals(environment.getServicesNameStem().get(0), GConstant.ALL_SERVICES)) {
                this.context.start(this.services.values(), this.chains);
            } else {
                this.context.start(getMatchedService(serviceNameList), this.chains);
            }
        } else {
            log.error("Not found a service to be run");
            throw new RuntimeException("Grampus: need to config a running service or use 'all' to start the all services'");
        }
    }

    private Collection<GService> getMatchedService(List<String> serviceNameList) {
        List<GService> matchingServices = new ArrayList<>();
        serviceNameList.forEach(serviceStem -> {
            if (services.containsKey(serviceStem)) {
                matchingServices.add(services.get(serviceStem));
            } else {
                log.warn("Cannot found a matched service [{}]", serviceStem);
            }
        });
        return matchingServices;
    }


    public void buildWorkflow() {
    }

    private void parseConfigService() {
        Map config = this.context.getGlobalConfig();
        config.keySet().forEach(key -> {
            Object configValue = config.get(key);
            if (configValue instanceof GService) {
                this.service((String) key, (GService) configValue);
            }
            try {
                if (this.services.containsKey(key) &&
                        configValue instanceof Map && ((Map<?, ?>) configValue).containsKey(GConstant.CELLS)) {
                    GService service = this.services.get(key);
                    Map<String, List<GCell>> cells = (Map<String, List<GCell>>) configValue;
                    cells.forEach((event, cellList) -> {
                        cellList.forEach(cell -> service.cell(event, cell));
                    });
                }
            } catch (Exception e) {
                log.error("Grampus: unable to parse config service [{}]", key, e);
            }
        });
    }

    public void test(Integer assertCount, Integer timeoutSeconds) {
        System.setProperty(GConstant.GRAMPUS_SERVICES, GConstant.ALL_SERVICES);
        GTester tester = new GTester(assertCount, timeoutSeconds);
        start(new DefaultTestEnvironment(tester));
    }

    public void test(Integer assertCount) {
        this.test(assertCount, 30);
    }

    public void test() {
        this.test(1);
    }

    public void assertTask(Runnable runnable) {
        this.context.addAssertTask(runnable);
    }
}

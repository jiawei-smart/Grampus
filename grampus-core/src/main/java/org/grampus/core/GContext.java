package org.grampus.core;

import org.grampus.util.GYamlUtil;
import org.grampus.log.GLogger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GContext {

    private Map<String, Object> workflowConfig;
    public final GRouter router = new GRouter();

    private GTester tester;


    public GContext(String configYaml) {
        Map config = GYamlUtil.load(configYaml);
        if(config != null){
            this.workflowConfig = config;
        }else {
            this.workflowConfig = new HashMap();
        }
    }

    public void start(Map<String, GService> services, List<String> chains) {
        router.parseWorkflowChain(chains);
        services.values().forEach(GService::initService);
        router.parseServiceEventChain(services);
        services.values().forEach(GService::initCells);
        services.values().forEach(GService::startCells);
        if(this.tester != null){
            GLogger.info("==Grampus start test model==");
            this.tester.start();
        }
        printInfo(services);
    }

    private void printInfo(Map<String, GService> services) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n");
        services.forEach((serviceName, gService) -> {
            stringBuilder.append("service: ["+serviceName+"]").append("\n");
            gService.getCells().forEach((event, gCells) -> {
                stringBuilder.append("  event:"+event.getEventStem()).append("\n");
                gCells.forEach(cell->{
                    stringBuilder.append("  --").append(cell.getId()).append("\n");
                });
            });
        });
        GLogger.info(stringBuilder.toString());
    }

//    public Future submitTask(Runnable runnable){
//        return this.executorService.submit(runnable);
//    }

    public Map<Object, Object> getServiceConfig(String name) {
        return (Map<Object, Object>) workflowConfig.get(name);
    }

    public Map<String, Object> getGlobalConfig() {
        return this.workflowConfig;
    }

    public void setTester(GTester gTester) {
        this.tester = gTester;
    }

    public void addAssertTask(Runnable runnable) {
        if(this.tester != null){
            this.tester.addAssertTask(runnable);
        }else {
            GLogger.error("the asserts only can be used in test model!");
        }
    }
}

package org.grampus.core;

import org.grampus.core.util.GYaml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class GContext {
    private ExecutorService executorService = Executors.newWorkStealingPool();
    private ExecutorService executorBlockingService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private Map workflowConfig;
    public final GRouter router = new GRouter();


    public GContext(String configYaml) {
        Map config = GYaml.load(configYaml);
        if(config != null){
            this.workflowConfig = config;
        }else {
            this.workflowConfig = new HashMap();
        }
    }


    protected void addChain(String chainStr){

    }

    public void start(Map<String, GService> services, List<String> chains) {
        router.parseWorkflowChain(chains);
        router.parseServiceEventChain(services);
        services.values().forEach(service->{
            service.initService();
            service.initCells();
        });
    }

    public Future submitTask(Runnable runnable){
        return this.executorService.submit(runnable);
    }

    public Future submitTask(Callable callable){
        return this.executorService.submit(callable);
    }

    public Future submitBlockingTask(Runnable runnable){
        return this.executorBlockingService.submit(runnable);
    }

    public Future submitBlockingTask(Callable callable){
        return this.executorBlockingService.submit(callable);
    }



    public Map<Object, Object> getServiceConfig(String name) {
        return (Map<Object, Object>) workflowConfig.get(name);
    }

    public Map<Object, Object> getGlobalConfig() {
        return this.workflowConfig;
    }

}

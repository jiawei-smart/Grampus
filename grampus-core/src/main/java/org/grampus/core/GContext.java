package org.grampus.core;

import org.grampus.core.messagebus.GMessageBus;
import org.grampus.core.messagebus.GMessageConsumer;
import org.grampus.core.messagebus.imp.GMessageBusImp;
import org.grampus.core.util.GYaml;
import org.grampus.log.GLogger;

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
    private Map<String, GService> services = new HashMap<>();
    private List<String> chains = new ArrayList<>();
    private Map workflowConfig;
    public final GRouter router = new GRouter();
    public final GMessageBus messageBus = new GMessageBusImp();

    public GContext(String workflowName, String configYaml) {
        Map config = GYaml.load(configYaml);
        if(config != null && config.containsKey(workflowName)){
            this.workflowConfig = (Map) config.get(workflowName);
            parseConfigService();
        }else {
            GLogger.error("Cannot found workflow config: [{}]",workflowName);
            this.workflowConfig = new HashMap();
        }
    }

    protected void registerService(String serviceName, GService service) {
        this.services.put(serviceName, service);
    }

    protected void addChain(String chainStr){
        this.chains.add(chainStr);
    }

    public void start() {
        router.registerWorkflowEventPath(this.chains);
        router.registerServiceEventPath(this.services);
        this.services.values().forEach(service->{
            service.initService(this);
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

    private void parseConfigService() {
        this.workflowConfig.keySet().forEach(key->{
            Object configValue = workflowConfig.get(key);
            if(configValue instanceof GService){
                this.registerService((String)key, (GService)configValue);
            }
        });
    }

    public String nextCellId(GEvent event, String cellId) {
       return router.getNextPathValue(event, cellId);
    }

    public void startConsume(GEvent event, GMessageConsumer consumer){
        this.messageBus.consume(event.toString(),consumer,false);
    }

    public void startConsume(GEvent event, GMessageConsumer consumer, boolean isWorker){
        this.messageBus.consume(event.toString(),consumer,isWorker);
    }
}

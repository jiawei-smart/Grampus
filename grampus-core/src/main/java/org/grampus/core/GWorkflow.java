package org.grampus.core;

import org.grampus.core.plugin.PluginService;
import org.grampus.log.GLogger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GWorkflow {

    private Map<String, GService> services = new HashMap<>();
    private List<String> chains = new ArrayList<>();
    private GContext context;

    private GTester tester;

    public GWorkflow() {
        this(GConstant.DEFAULT_WORKFLOW_NAME,GConstant.DEFAULT_CONFIG_YAML);
    }

    public GWorkflow(String workflowName, String configYaml){
        context = new GContext(configYaml);
    }

    public GService service(String serviceName, GService service) {
        service.setName(serviceName);
        service.setContext(this.context);
        this.services.put(serviceName, service);
        return service;
    }

    public GService service(String serviceName) {
       return this.service(serviceName, new GService(serviceName));
    }

    public GWorkflow chain(String... chains){
        if(chains != null && chains.length > 0){
            for (String chain : chains){
                this.chains.add(chain);
            }
        }
        return this;
    }

    public String link(String chainStartPoint,String chainEndPoint){
        return chainEndPoint+GConstant.CHAIN_SPLIT_CHAR+chainEndPoint;
    }

    public String event(String serviceName, String event ){
        return serviceName+"."+event;
    }

    public void start(){
        buildWorkflow();
        this.service(GConstant.PLUGIN_SERVICE, new PluginService());
        this.context.start(this.services, this.chains);
    }

    public void buildWorkflow() {
    }

    private void parseConfigService() {
        Map config = this.context.getGlobalConfig();
        config.keySet().forEach(key->{
            Object configValue = config.get(key);
            if(configValue instanceof GService){
                this.service((String)key, (GService)configValue);
            }
        });
    }

    public void test(Integer assertCount, Integer timeoutSeconds){
        this.tester = new GTester(assertCount);
        start();
        this.tester.start(timeoutSeconds);
    }

    public void test(Integer assertCount){
        this.test(assertCount,30);
    }

    public void test(){
       this.test(1);
    }

    public void asserts(Runnable runnable){
        if(this.tester != null){
            this.tester.addAssertTask(runnable);
        }else {
            GLogger.error("the asserts only can be used in test model!");
        }
    }


}

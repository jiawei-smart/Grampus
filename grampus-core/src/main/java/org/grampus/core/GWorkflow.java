package org.grampus.core;

import org.grampus.core.plugin.PluginService;

public class GWorkflow {
    private GContext context;

    public GWorkflow() {
        this(GConstant.DEFAULT_WORKFLOW_NAME,GConstant.DEFAULT_CONFIG_YAML);
    }

    public GWorkflow(String workflowName, String configYaml){
        context = new GContext(workflowName, configYaml);
    }

    public GWorkflow service(String serviceName, GService service) {
        this.context.registerService(serviceName, service);
        return this;
    }

    public GWorkflow service(String serviceName) {
       return this.service(serviceName, new GService());
    }

    public GWorkflow chain(String... chains){
        if(chains != null && chains.length > 0){
            for (String chain : chains){
                this.context.addChain(chain);
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
        this.context.registerService(GConstant.PLUGIN_SERVICE, new PluginService());
    }




}

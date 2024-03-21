package org.grampus.core;

import org.grampus.log.GLogger;

public class GStarter {
    public static void main(String[] args) {
        String workflowClassPath = args[0];
        String configYaml = GConstant.DEFAULT_CONFIG_YAML;
        if(args.length > 1){
            configYaml = args[1];
        }
        try {
            Class<GWorkflow> workflowClass = (Class<GWorkflow>) Class.forName(workflowClassPath);
            GWorkflow workflow = workflowClass.getConstructor(String.class).newInstance(configYaml);
            workflow.start();
        } catch (Exception e) {
            GLogger.error("failure to start Grampus with {}", e);
            throw new RuntimeException(e);
        }
    }
}

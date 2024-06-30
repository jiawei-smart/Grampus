package org.grampus.core;

import org.grampus.log.GLogger;

public class GStarter {
    public static void main(String[] args) {
        String workflowClassPath = args[0];
        if (args.length < 0 ) {
            throw new RuntimeException("Please specify a workflow class");
        }
        try {
            Class<GWorkflow> workflowClazz = (Class<GWorkflow>) Class.forName(workflowClassPath);
            GWorkflow workflow = workflowClazz.getConstructor(String.class).newInstance();
            workflow.startAll();
        } catch (Exception e) {
            GLogger.error("failure to start Grampus with {}", e);
        }
    }
}

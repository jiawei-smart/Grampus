package org.grampus.fix.test;

import org.grampus.core.GWorkflow;
import org.grampus.fix.GFixProcessor;

public class AcceptorWorkflow {
    public static void main(String[] args) {
        GWorkflow workflow = new GWorkflow(){
            @Override
            public void buildWorkflow() {
                service("ACCEPT_SERVICE").process(new GFixProcessor());
            }
        };
        workflow.start();
    }
}

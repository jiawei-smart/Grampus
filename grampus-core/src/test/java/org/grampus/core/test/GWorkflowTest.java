package org.grampus.core.test;

import org.grampus.core.GCell;
import org.grampus.core.GWorkflow;
import org.grampus.core.message.GMessage;
import org.junit.Test;

public class GWorkflowTest {

    @Test
    public void workflowTest(){

    }

    public static void main(String[] args) {
        GWorkflow workflow = new GWorkflow();
        workflow.service("TEST_SERVICE").cell("EVENT_1", new GCell(){
            @Override
            public void init() {
                onEvent("EVENT_2", "test message !");
            }
        }).cell("EVENT_2", new GCell(){
            @Override
            protected void handle(GMessage message) {
                System.out.println(message.getPayload());
            }
        });
        workflow.start();
    }
}

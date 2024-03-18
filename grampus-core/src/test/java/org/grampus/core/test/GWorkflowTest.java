package org.grampus.core.test;

import org.grampus.core.GCell;
import org.grampus.core.GWorkflow;
import org.grampus.core.message.GMessage;
import org.junit.Test;

import java.util.Map;

public class GWorkflowTest {

    @Test
    public void workflowTest() {

    }

    public static void main(String[] args) {
        GWorkflow workflow = new GWorkflow();
        workflow.service("TEST_SERVICE1")
                .cell(new GCell(){
                    @Override
                    public void init() {
                        System.out.println("=S1=start");
                        onEvent("EVENT_0", "S1-default_0");
                    }
                })
                .cell("EVENT_0", new GCell(){
                    @Override
                    public Object handle(String from, Object payload, Map meta) {
                        System.out.println("=S1=EVENT_0_P0 received "+payload);
                        return payload+"->EVENT_0_P0";
                    }
                })
                .cell("EVENT_0", new GCell(){
                    @Override
                    public void handle(Object payload, Map meta) {
                        System.out.println("=S1=EVENT_0_P1 received "+payload);
                        onEvent("EVENT_1",  payload+"->EVENT_0_P1");
                    }
                })
                .cell("EVENT_1", new GCell() {
                    @Override
                    public void handle(Object payload, Map meta) {
                        System.out.println("=S1=EVENT_1_P0 received "+payload);
                        onEvent("EVENT_2",  payload+"->EVENT_1_P0");
                    }
                }).cell("EVENT_2", new GCell() {
                    @Override
                    public void handle(Object payload, Map meta) {
                        System.out.println("=S1=EVENT_2_P0 received "+payload);
                        System.out.println("S1 message: "+payload+"->EVENT_2_P0");
                        onEvent("EVENT_3",  payload+"->EVENT_3_P0");
                    }
                }).openEvent("EVENT_3");

        workflow.service("TEST_SERVICE2").cell(new GCell(){
            @Override
            public void handle(Object payload, Map meta) {
                System.out.println("=S2=EVENT_1_P0 received " + payload+"==>S2_EVENT_3");
                onEvent("EVENT_3",  payload+"==>S2_default");
            }
        }).cell("EVENT_3",new GCell(){
            @Override
            public void handle(Object payload, Map meta) {
                System.out.println( "S2 message: "+payload+"->EVENT_3");
            }
        });

        workflow.chain("TEST_SERVICE1.EVENT_2->TEST_SERVICE2");
        workflow.start();
    }
}

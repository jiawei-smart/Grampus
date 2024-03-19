package org.grampus.core.test;

import org.grampus.core.GCell;
import org.grampus.core.GWorkflow;
import org.grampus.core.message.GMessage;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

public class GWorkflowTest {

    @Test
    public void workflowTest() {
        GWorkflow workflow = new GWorkflow(){
            @Override
            public void buildWorkflow() {
                service("S1")
                        .cell(new GCell(){
                            @Override
                            public void init() {
                                onEvent("E0", "message");
                            }
                        })
                        .cell("E0", new GCell(){
                            @Override
                            public Object handle(String from, Object payload, Map meta) {
                                asserts(()->Assert.assertEquals("message", payload));
                                return payload+"->E0_P0";
                            }
                        })
                        .cell("E0", new GCell(){
                            @Override
                            public void handle(Object payload, Map meta) {
                                asserts(()->Assert.assertEquals("message->E0_P0", payload));
                                onEvent("E1",  payload+"->E0_P1");
                            }
                        })
                        .cell("E1", new GCell() {
                            @Override
                            public void handle(Object payload, Map meta) {
                                asserts(()->Assert.assertEquals("message->E0_P0->E0_P1", payload));
                                onEvent("E2",  payload+"->E1_P0");
                            }
                        }).cell("E2", new GCell() {
                            @Override
                            public void handle(Object payload, Map meta) {
                               asserts(()-> Assert.assertEquals("message->E0_P0->E0_P1->E1_P0", payload));
                                onEvent("E3",  payload+"->E3_P0");
                            }
                        }).openEvent("E3");

                service("S2").cell(new GCell(){
                    @Override
                    public void handle(Object payload, Map meta) {
                        asserts(()->Assert.assertEquals("message->E0_P0->E0_P1->E1_P0->E3_P0", payload));
                        onEvent("E3",  payload+"==>S2_default");
                    }
                }).cell("E3",new GCell(){
                    @Override
                    public void handle(Object payload, Map meta) {
                        asserts(()->Assert.assertEquals("message->E0_P0->E0_P1->E1_P0->E3_P0==>S2_default", payload));
                    }
                });

                chain("S1.E3->S2");
            }
        };

        workflow.test(6);
    }
}

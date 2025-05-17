package org.grampus.core.test;

import org.grampus.core.GProcessor;
import org.grampus.core.GService;
import org.grampus.core.GWorkflow;
import org.grampus.core.message.GMessageHeader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class GWorkflowTest {

    private static final Logger log = LoggerFactory.getLogger(GWorkflowTest.class);

    @Test
    public void workflowTest() {
        GWorkflow workflow = new GWorkflow() {
            @Override
            public void buildWorkflow() {
                service("S1")
                        .process(new GProcessor() {
                            @Override
                            public void start() {
                                onEvent("E0", "message");
                            }
                        })
                        .process("E0", new GProcessor() {
                            @Override
                            public Object handle(GMessageHeader header, Object payload, Map meta) {
                                assertTask(() -> Assertions.assertEquals("message", payload));
                                log.info("E0 payload: {}", payload);
                                return payload + "->E0_P0";
                            }
                        })
                        .process("E0", new GProcessor() {
                            @Override
                            public void handle(Object payload, Map meta) {
                                assertTask(() -> Assertions.assertEquals("message->E0_P0", payload));
                                log.info("E0 payload: {}", payload);
                                onEvent("E1", payload + "->E0_P1");
                            }
                        })
                        .process("E1", new GProcessor() {
                            @Override
                            public void handle(Object payload, Map meta) {
                                assertTask(() -> Assertions.assertEquals("message->E0_P0->E0_P1", payload));
                                onEvent("E2", payload + "->E1_P0");
                            }
                        }).process("E2", new GProcessor() {
                            @Override
                            public void handle(Object payload, Map meta) {
                                assertTask(() -> Assertions.assertEquals("message->E0_P0->E0_P1->E1_P0", payload));
                                onEvent("E3", payload + "->E3_P0");
                            }
                        }).openEvent("E3");

                service("S2").process(new GProcessor() {
                    @Override
                    public void handle(Object payload, Map meta) {
                        assertTask(() -> Assertions.assertEquals("message->E0_P0->E0_P1->E1_P0->E3_P0", payload));
                        onEvent("E4", payload + "==>S2_default");
                    }
                }).process("E4", new GProcessor() {
                    @Override
                    public void handle(Object payload, Map meta) {
                        assertTask(() -> Assertions.assertEquals("message->E0_P0->E0_P1->E1_P0->E3_P0==>S2_default", payload));
                    }
                });

                service("S3").process("E5", new GProcessor() {
                    @Override
                    public void handle(Object payload, Map meta) {
                        assertTask(() -> Assertions.assertEquals("message->E0_P0->E0_P1->E1_P0->E3_P0", payload));
                    }
                });

                chain("S1.E3->S2");
                chain(link(event("S1", "E3"), event("S3", "E5")));
            }
        };

        workflow.test(7);
    }

    @Test
    public void workflowReactiveTest() {
        GWorkflow workflow = new GWorkflow() {
            @Override
            public void buildWorkflow() {
                GService service = service("S1");

                GProcessor msgSourceCell = new GProcessor() {
                    public void start() {
                        onEvent("E1", "message");
                    }

                    @Override
                    public void handle(Object payload, Map meta) {
                        assertTask(()->Assertions.assertEquals("ACK->message->P1->P2(map)->P3(Redirect)",payload));
                    }
                };

                service.source("HELLE_WORLD",msgSourceCell)
                        .listen("E1")
                        .then((header, payload, meta) -> {
                            assertTask(() -> Assertions.assertEquals("message", payload));
                            return payload + "->P1";
                        })
                        .map((payload, meta) -> payload + "->P2(map)")
                        .redirect("E2");


                service.listen("E2")
                        .then((header,payload, meta)->payload+"->P3(Redirect)")
                        .sink(new GProcessor() {
                            @Override
                            public void handle(Object payload, Map meta) {
                                assertTask(()->Assertions.assertEquals("message->P1->P2(map)->P3(Redirect)", payload));
                                onEvent("HELLE_WORLD","ACK->"+payload);
                            }
                        });
            }
        };
        workflow.test(3);
    }
}

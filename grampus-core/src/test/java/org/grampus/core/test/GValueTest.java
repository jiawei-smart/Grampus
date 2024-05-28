package org.grampus.core.test;

import org.grampus.core.GCell;
import org.grampus.core.GWorkflow;
import org.grampus.core.annotation.config.GEnvValue;
import org.grampus.core.annotation.config.GValue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GValueTest {

    @Test
    public void testConfigProperty() {
        System.getProperties().setProperty("myEnv","test");
        System.getProperties().setProperty("user","Tom");
        GWorkflow workflow = new GWorkflow() {
            @Override
            public void buildWorkflow() {
                service("S1")
                        .source(new GCell() {
                            @GValue("S1.key")
                            private String key;

                            @GValue("S1.env")
                            private String env;

                            @GEnvValue("user")
                            private String user;

                            @Override
                            public void start() {
                                onEvent("E1", key+"-"+env+":"+user);
                            }
                        })
                        .listen("E1").sink((payload, meta) -> {
                            assertTask(() -> Assertions.assertEquals(payload, "value-test:Tom"));
                        });
            }
        };
        workflow.test();
    }
}

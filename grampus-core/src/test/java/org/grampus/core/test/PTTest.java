package org.grampus.core.test;

import org.grampus.core.GProcessor;
import org.grampus.core.GProcessorOptions;
import org.grampus.core.GWorkflow;
import org.grampus.core.message.GMessage;
import org.grampus.log.GLogger;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class PTTest {
    public static void main(String[] args) {
        GWorkflow workflow = new GWorkflow() {
            long startTime;

            Integer messageSize = 10000;

            Integer parallel = 1;

            @Override
            public void buildWorkflow() {
                service("S1").process(new GProcessor() {
                    @Override
                    public void start() {
                        startTime = now();
                        getController().submitTask(()->{
                            for (int i = 0; i < messageSize; i++) {
                                onEvent("E0", i+1);
                            }
                        });

                    }
                }).openEvent("E0");

                GProcessorOptions cellOptions = new GProcessorOptions();
                cellOptions.setParallel(parallel);
                service("S2").process(new GProcessor(cellOptions) {
                    Random random = new Random();
                    AtomicInteger receivedMsgCount = new AtomicInteger(0);

                    @Override
                    public void start() {
                        getController().createTimer(() -> System.out.println("******received**********" + receivedMsgCount.get())).schedule(5l, TimeUnit.SECONDS);
                    }

                    @Override
                    public void handle(Object payload, Map meta) {
                        try {
                            Thread.sleep(10);
                            receivedMsgCount.addAndGet(1);
                            GLogger.info("**** process msg seq [{}],total msgCount [{}], total cost time [{}], avg [{}]", payload, receivedMsgCount.get(), now() - startTime, (now() - startTime)/receivedMsgCount.get());
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public int parallelBy(GMessage message) {
                       return (Integer) message.payload()%parallel;
                    }
                });

                chain("S1.E0->S2");
            }
        };
        workflow.startAll();
    }
}

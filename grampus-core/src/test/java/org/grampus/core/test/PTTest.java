package org.grampus.core.test;

import org.grampus.core.GCell;
import org.grampus.core.GCellOptions;
import org.grampus.core.GTimer;
import org.grampus.core.GWorkflow;
import org.grampus.log.GLogger;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class PTTest {
    public static void main(String[] args) {
        GWorkflow workflow = new GWorkflow() {
            long startTime;
            GTimer timer;

            Integer messageSize = 10000;

            Integer parallel = 30;

            @Override
            public void buildWorkflow() {
                service("S1").cell(new GCell() {
                    @Override
                    public void start() {
                        AtomicInteger i = new AtomicInteger(1);
                        timer = getController().createTimer(() -> {
                            if (i.get() <= messageSize) {
                                onEvent("E0", i.addAndGet(1));
                            } else {
                                timer.cancel();
                            }
                        }).schedule(1L, TimeUnit.MILLISECONDS);
                        startTime = now();
                        GLogger.info("**** start *****");
                        getController().createTimer(() -> System.out.println("******send******" + (i.get() - 1))).schedule(5l, TimeUnit.SECONDS);

                    }
                }).openEvent("E0");

                GCellOptions cellOptions = new GCellOptions();
                cellOptions.setParallel(parallel);
                service("S2").cell(new GCell(cellOptions) {
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
                            GLogger.info("**** process msg seq [{}],total msgCount [{}], total cost time [{}]", payload, receivedMsgCount.get(), now() - startTime);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });

                chain("S1.E0->S2");
            }
        };
        workflow.start();
    }
}

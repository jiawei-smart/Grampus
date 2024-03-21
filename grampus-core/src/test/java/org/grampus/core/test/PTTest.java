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

            Integer messageSize = 1000;

            @Override
            public void buildWorkflow() {
                service("S1").cell(new GCell() {
                    @Override
                    public void init() {
                        AtomicInteger i = new AtomicInteger(1);
                        timer = getController().createTimer(() -> {
                            if(i.get() <= messageSize){
                                onEvent("E0", i.getAndIncrement());
                            }else {
                                timer.cancel();
                            }
                        }).schedule(1L, TimeUnit.MILLISECONDS);
                        startTime = now();
                        GLogger.info("**** start");
                    }
                }).openEvent("E0");

                GCellOptions cellOptions = new GCellOptions();
                cellOptions.setParallel(40);
                service("S2").cell(new GCell(cellOptions) {
                    Random random = new Random();

                    @Override
                    public void handle(Object payload, Map meta) {

                        try {
                            Thread.sleep(10);
                            GLogger.info("**** process [{}], total cost time [{}]", payload, now() - startTime);
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

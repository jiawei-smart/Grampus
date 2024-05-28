package org.grampus.core.test;

import org.grampus.core.GCell;
import org.grampus.core.GWorkflow;
import org.grampus.log.GLogger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;

public class GWorkerExecutorOrderTest {
    @Test
    public void workflowTest() {
        Integer messageCount = 1000;
        Integer maxPendingMills = 50;
        GWorkflow workflow = new GWorkflow() {
            @Override
            public void buildWorkflow() {
                service("SERVICE_1").cell(new GCell() {
                    AtomicInteger task1MsgSeq = new AtomicInteger(1);
                    AtomicInteger  task2MsgSeq = new AtomicInteger(1);
                    AtomicInteger  task3MsgSeq = new AtomicInteger(1);
                    Random random = new Random();

                    @Override
                    public void start() {
                        getController().submitBlockingTask(() -> doTask1());
                        getController().submitBlockingTask(() -> doTask2());
                        getController().submitBlockingTask(() -> doTask3());
                    }

                    private void doTask1() {
                        Executor executor = getController().getSingleExecutor();
                        while (task1MsgSeq.get() <= messageCount) {
                            executor.execute(()->sendMsg("TASK1",task1MsgSeq));
                            try {
                                Thread.sleep(random.nextInt(maxPendingMills));
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        GLogger.info("TASK1 msg send completed");
                    }

                    private void doTask2() {
                        Executor executor = getController().getSingleExecutor();
                        while (task2MsgSeq.get() <= messageCount) {
                            executor.execute(()->sendMsg("TASK2",task2MsgSeq));
                            try {
                                Thread.sleep(random.nextInt(maxPendingMills));
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        GLogger.info("TASK2 msg send completed");
                    }

                    private void doTask3() {
                        Executor executor = getController().getSingleExecutor();
                        while (task3MsgSeq.get() <= messageCount) {
                            executor.execute(()->sendMsg("TASK2",task2MsgSeq));
                            try {
                                Thread.sleep(random.nextInt(maxPendingMills));
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        GLogger.info("TASK3 msg send completed");
                    }

                    public void sendMsg(String event, AtomicInteger msgSeq){
                        onEvent(event, msgSeq.getAndIncrement());
                    }
                }).openEvent("TASK1", "TASK2", "TASK3");
                service("SERVICE_2")
                        .cell("TASK1", new MsgReceiver())
                        .cell("TASK2", new MsgReceiver())
                        .cell("TASK3", new MsgReceiver());
                chain("SERVICE_1.TASK1->SERVICE_2.TASK1", "SERVICE_1.TASK2->SERVICE_2.TASK2", "SERVICE_1.TASK3->SERVICE_2.TASK3");
            }
        };
        workflow.test(messageCount, (messageCount*maxPendingMills*2)/1000);
    }


}

class MsgReceiver extends GCell<Integer> {
    Integer expectedMsgASeq = 1;

    @Override
    public void handle(Integer payload, Map meta) {
        boolean expectedMsgSeq = expectedMsgASeq.equals(payload);
        if (!expectedMsgSeq) {
            GLogger.error(" message ordering issue , expectedMsgASeq {},  payload {}", expectedMsgASeq, payload);
        }
        assertTask(() -> Assertions.assertTrue(expectedMsgSeq));
        expectedMsgASeq++;
    }
}

package org.grampus.core.test;

import org.grampus.core.GCell;
import org.grampus.core.GWorkflow;
import org.grampus.log.GLogger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Random;

public class MesasgeOrderTest {
    @Test
    public void workflowTest() {
        Integer messageCount = 1000;
        Integer maxPendingMills = 50;
        GWorkflow workflow = new GWorkflow() {
            @Override
            public void buildWorkflow() {
                service("SERVICE_1").cell(new GCell() {
                    Integer task1MsgSeq = 1;
                    Integer task2MsgSeq = 1;
                    Integer task3MsgSeq = 1;
                    Random random = new Random();

                    @Override
                    public void start() {
                        getController().submitBlockingTask(() -> doTask1());
                        getController().submitBlockingTask(() -> doTask2());
                        getController().submitBlockingTask(() -> doTask3());
                    }

                    private void doTask1() {
                        while (task1MsgSeq <= messageCount) {
                            onEvent("TASK1", task1MsgSeq);
                            task1MsgSeq++;
                            try {
                                Thread.sleep(random.nextInt(maxPendingMills));
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        GLogger.info("TASK1 msg send completed");
                    }

                    private void doTask2() {
                        while (task2MsgSeq <= messageCount) {
                            onEvent("TASK2", task2MsgSeq);
                            task2MsgSeq++;
                            try {
                                Thread.sleep(random.nextInt(maxPendingMills));
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        GLogger.info("TASK2 msg send completed");
                    }

                    private void doTask3() {
                        while (task3MsgSeq <= messageCount) {
                            onEvent("TASK3", task3MsgSeq);
                            task3MsgSeq++;
                            try {
                                Thread.sleep(random.nextInt(maxPendingMills));
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        GLogger.info("TASK3 msg send completed");
                    }
                }).openEvent("TASK1", "TASK2", "TASK3");
                service("SERVICE_2")
                        .cell("TASK1", new MsgReceiveCell())
                        .cell("TASK2", new MsgReceiveCell())
                        .cell("TASK3", new MsgReceiveCell());
                chain("SERVICE_1.TASK1->SERVICE_2.TASK1", "SERVICE_1.TASK2->SERVICE_2.TASK2", "SERVICE_1.TASK3->SERVICE_2.TASK3");
            }
        };
        workflow.test(messageCount, (messageCount*maxPendingMills*2)/1000);
    }


}

class MsgReceiveCell extends GCell<Integer> {
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

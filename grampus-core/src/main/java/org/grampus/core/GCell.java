package org.grampus.core;

import org.grampus.core.message.GMessage;
import org.grampus.core.messagebus.GMessageConsumer;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class GCell {
    private String id;
    private GEvent event;
    private int parallel = 1;
    private int batchSize = 1;
    private int batchTimer = 0;
    private BlockingQueue<GMessage> messageQueue = new LinkedBlockingDeque<>();
    private Set<String> parallelConsumerTopics = new HashSet<>();
    private GContext context;
    private Set<GEvent> nextEvents;

    private enum CELL_ACTION {DATA_PUSH, TIMER_OFFSET}

    private Random pnoCreator = new Random();

    void initCell(GContext context) {
        this.context = context;
        context.submitBlockingTask(this::init);
        context.messageBus.consume(this.id, new GMessageConsumer() {
            @Override
            public void handle(GMessage message) {
                message.getHeader().updateTimestamp(id, "IN");
                offset(CELL_ACTION.DATA_PUSH, message);
            }
        });
    }

    private void offset(CELL_ACTION cellAction, GMessage message) {
        if (cellAction == CELL_ACTION.DATA_PUSH) {
            this.messageQueue.offer(message);
            if (messageQueue.size() == batchSize) {
                this.drainTo(batchSize);
            }
        } else if (cellAction == CELL_ACTION.TIMER_OFFSET) {
            int size = messageQueue.size();
            this.drainTo(size > batchSize ? batchSize : size);
        }
    }

    private void drainTo(int batchSize) {
        List<GMessage> messages = new ArrayList<>();
        this.messageQueue.drainTo(messages, batchSize);
        messages.forEach(message -> {
            int pno = parallelBy(message);
            this.context.messageBus.publish(this.id + "_pno_" + pno, message);
        });
    }

    public int parallelBy(GMessage message) {
        return pnoCreator.nextInt(parallel);
    }

    public void setParallel(int i) {
        this.parallel = 1;
        initParallelConsumer();
    }

    private void initParallelConsumer() {
        for (int i = 0; i < this.parallel; i++) {
            String parallelConsumeTopic = this.id + "_pno_" + i;
            if (!this.parallelConsumerTopics.contains(parallelConsumeTopic)) {
                this.context.messageBus.consume(parallelConsumeTopic, this::handle, false);
            }
        }
    }

    protected void handle(GMessage message) {
        Object out = handle(message.getHeader().getCurrentCellId(), message.getPayload(), message.meta());
        message.getHeader().update(this.event, this.id);
        onEvent(nextEvents, out);
    }

    private void onEvent(Set<GEvent> nextEvents, Object out) {
        nextEvents.forEach(event -> {
            context.router.getNextPathValue(event, id);
        });
    }

    public void onEvent(String event, Object message){
        GMessage gMessage = new GMessage();
        gMessage.setPayload(message);
        context.messageBus.publish(context.router.getNextPathValue(this.event.getService(),event,id),gMessage);
    }

    public Object handle(String from, Object payload, Map meta) {
        handle(payload, meta);
        return payload;
    }

    public void handle(Object payload, Map meta) {
    }

    public void init() {

    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public GEvent getEvent() {
        return event;
    }

    public void setEvent(GEvent event) {
        this.event = event;
    }

    public BlockingQueue<GMessage> getMessageQueue() {
        return messageQueue;
    }

    public void setMessageQueue(BlockingQueue<GMessage> messageQueue) {
        this.messageQueue = messageQueue;
    }

    public void setNextEvents(Set<GEvent> nextEvents) {
        this.nextEvents = nextEvents;
    }
}

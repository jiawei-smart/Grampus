package org.grampus.core;

import org.grampus.core.message.GMessage;
import org.grampus.core.messagebus.GMessageConsumer;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class GCell {
    private String id;
    private GEvent event;
    private int parallel = 1;
    private int batchSize = 1;
    private int batchInterval = 0;
    private BlockingQueue<GMessage> messageQueue = new LinkedBlockingDeque<>();

    void initCell(GContext context){
        context.submitBlockingTask(this::init);
        context.messageBus.consume(this.id, new GMessageConsumer() {
            @Override
            public void handle(GMessage message) {
                messageQueue.add(message);
            }
        });
    }

    public void init(){

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
}

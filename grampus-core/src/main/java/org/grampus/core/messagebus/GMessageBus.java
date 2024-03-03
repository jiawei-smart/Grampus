package org.grampus.core.messagebus;

import org.grampus.core.message.GMessage;

public interface GMessageBus {
    default void consume(String topic, GMessageConsumer consumer){
        consume(topic,consumer,false);
    }
    void consume(String topic, GMessageConsumer consumer, boolean isWorker);
    void publish(String topic, GMessage message);
}

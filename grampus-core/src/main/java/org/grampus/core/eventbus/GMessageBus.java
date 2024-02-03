package org.grampus.core.eventbus;

import org.grampus.core.message.GMessage;

public interface GMessageBus {
    void consume(String topic, GMessageBustHandler consumer, boolean isWorker);
    void publish(String topic, GMessage message);

}

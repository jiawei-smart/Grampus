package org.grampus.core.messagebus.imp;

import org.grampus.core.message.GMessage;
import org.grampus.core.messagebus.GMessageBus;
import org.grampus.core.messagebus.GMessageConsumer;

public class GMessageBusChronicleImp implements GMessageBus {
    @Override
    public void consume(String topic, GMessageConsumer consumer, boolean isWorker) {

    }

    @Override
    public void publish(String topic, GMessage message) {

    }
}

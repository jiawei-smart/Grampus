package org.grampus.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface GKafkaMsgHandler {
    void onConsumerRecord(ConsumerRecord record);
}

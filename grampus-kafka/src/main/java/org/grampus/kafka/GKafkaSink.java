package org.grampus.kafka;

import org.grampus.log.GLogger;

public class GKafkaSink<T> extends GKafkaProcessor<T> {

    @Override
    public void start() {
        onStatus("kafka init",false);
        GKafkaClient client = new GKafkaClient(null);
        GKafkaOptions config = getConfig(GKafkaOptions.class);
        if (config != null) {
            config.isConsumerConfigured(false);
            client.start(config);
            setClient(client);
        }else {
            GLogger.error("failed to start kafka sink due to config as null, {}",getId());
        }
        onStatus("kafka init",true);
    }
}

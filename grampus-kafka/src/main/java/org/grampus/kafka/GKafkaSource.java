package org.grampus.kafka;

import org.grampus.log.GLogger;

public class GKafkaSource<T> extends GKafkaCell<T>{

    @Override
    public void start() {
        onStatus("kafka init",false);
        GKafkaClient client = new GKafkaClient(null);
        GKafkaOptions config = getConfig(GKafkaOptions.class);
        if (config != null) {
            config.isProducerConfigured(false);
            client.start(config);
            setClient(client);
        }else {
            GLogger.error("failed to start kafka sink due to config as null, {}",getId());
        }
        onStatus("kafka init",true);
    }
}

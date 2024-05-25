package org.grampus.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.grampus.core.GCell;
import org.grampus.log.GLogger;

import java.util.Map;

public class GKafkaCell<T> extends GCell<T> implements GKafkaMsgHandler{
    public static final String KAFKA_CONFIG_YAML = "kafkaConfig.yaml";
    public static final String KAFKA_CONFIG = "kafkaConfig";
    public static final String FROM_KAFKA = "FROM_KAFKA";
    public static final String KAFKA_PRODUCER_KEY = "KAFKA_PRODUCER_KEY";
    private GKafkaClient client;

    @Override
    public void start() {
        onStatus("kafka init",false);
        this.client = new GKafkaClient(this::onConsumerRecord);
        GKafkaOptions config = getConfig(GKafkaOptions.class);
        if(config != null){
            this.client.start(config);
        }else {
            GLogger.error("failed to start kafka client due to config as null, {}",getId());
        }
        onStatus("kafka init",true);
    }

    @Override
    public void onConsumerRecord(ConsumerRecord record) {
        onEvent(FROM_KAFKA, record.value());
    }

    @Override
    public void handle(T payload, Map meta) {
        if(this.client != null){
            this.client.send(this.getProducerKey(payload,meta),payload);
        }else {
            GLogger.debug("kafka client as null, failure to handle message {}",payload);
        }
    }

    public Object getProducerKey(T payload, Map meta) {
        return meta.get(KAFKA_PRODUCER_KEY);
    }

    @Override
    public String getConfigKey() {
        return KAFKA_CONFIG;
    }

    @Override
    public String getConfigFileKey() {
        return KAFKA_CONFIG_YAML;
    }

    public GKafkaClient getClient() {
        return client;
    }

    public void setClient(GKafkaClient client) {
        this.client = client;
    }
}

package org.grampus.kafka;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.config.SaslConfigs;
import org.grampus.core.client.GClientConfig;
import org.grampus.util.GStringUtil;

import java.util.Properties;
import java.util.UUID;

public class GKafkaOptions implements GClientConfig {
    public static final String KAFKA_CONFIG = "kafkaConfig";
    private String producerTopic;
    private String bootstrapServers;
    private String keySerializer = "org.apache.kafka.common.serialization.StringSerializer";
    private String valueSerializer = "org.apache.kafka.common.serialization.StringSerializer";
    ;
    private String producerBatchSize;
    private String producerBufferMemory;
    private String heartbeatIntervalMs;
    private String clientId;
    private String saslMechanism = "SCRAM-SHA-256";
    private String username;
    private String password;
    private String saslJaasConfigClass = "org.apache.kafka.common.security.scram.ScramLoginModule";
    private String consumerTopic;
    private String keyDeserializer = "org.apache.kafka.common.serialization.StringSerializer";
    private String valueDeserializer = "org.apache.kafka.common.serialization.StringSerializer";
    private String consumerBatchSize;
    private String groupId;
    private String consumerBufferMemory;
    private String securityProtocol = "SASL_PLAINTEXT";
    private long consumeIntervalMills = 300l;

    private long startSeq = -1;
    private boolean isSeekToBeginningConsume = false;
    private boolean isSeekToEndConsume = false;
    private boolean specificAvroReader = true;
    private String consumeDuration;
    private String consumerPartitions; // partition concat by char "," group concat by char ";"
    private String schemaRegisterUrl;

    @Override
    public String getConfigKey() {
        return KAFKA_CONFIG;
    }

    public String getProducerTopic() {
        return producerTopic;
    }

    public void setProducerTopic(String producerTopic) {
        this.producerTopic = producerTopic;
    }

    public String getBootstrapServers() {
        return bootstrapServers;
    }

    public void setBootstrapServers(String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }

    public String getKeySerializer() {
        return keySerializer;
    }

    public void setKeySerializer(String keySerializer) {
        this.keySerializer = keySerializer;
    }

    public String getValueSerializer() {
        return valueSerializer;
    }

    public void setValueSerializer(String valueSerializer) {
        this.valueSerializer = valueSerializer;
    }

    public String getProducerBatchSize() {
        return producerBatchSize;
    }

    public void setProducerBatchSize(String producerBatchSize) {
        this.producerBatchSize = producerBatchSize;
    }

    public String getProducerBufferMemory() {
        return producerBufferMemory;
    }

    public void setProducerBufferMemory(String producerBufferMemory) {
        this.producerBufferMemory = producerBufferMemory;
    }

    public String getHeartbeatIntervalMs() {
        return heartbeatIntervalMs;
    }

    public void setHeartbeatIntervalMs(String heartbeatIntervalMs) {
        this.heartbeatIntervalMs = heartbeatIntervalMs;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getSaslMechanism() {
        return saslMechanism;
    }

    public void setSaslMechanism(String saslMechanism) {
        this.saslMechanism = saslMechanism;
    }

    public String getConsumerTopic() {
        return consumerTopic;
    }

    public void setConsumerTopic(String consumerTopic) {
        this.consumerTopic = consumerTopic;
    }

    public String getKeyDeserializer() {
        return keyDeserializer;
    }

    public void setKeyDeserializer(String keyDeserializer) {
        this.keyDeserializer = keyDeserializer;
    }

    public String getValueDeserializer() {
        return valueDeserializer;
    }

    public void setValueDeserializer(String valueDeserializer) {
        this.valueDeserializer = valueDeserializer;
    }

    public String getConsumerBatchSize() {
        return consumerBatchSize;
    }

    public void setConsumerBatchSize(String consumerBatchSize) {
        this.consumerBatchSize = consumerBatchSize;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getConsumerBufferMemory() {
        return consumerBufferMemory;
    }

    public void setConsumerBufferMemory(String consumerBufferMemory) {
        this.consumerBufferMemory = consumerBufferMemory;
    }

    public String getSecurityProtocol() {
        return securityProtocol;
    }

    public void setSecurityProtocol(String securityProtocol) {
        this.securityProtocol = securityProtocol;
    }

    public long getConsumeIntervalMills() {
        return consumeIntervalMills;
    }

    public void setConsumeIntervalMills(long consumeIntervalMills) {
        this.consumeIntervalMills = consumeIntervalMills;
    }

    public long getStartSeq() {
        return startSeq;
    }

    public void setStartSeq(long startSeq) {
        this.startSeq = startSeq;
    }

    public boolean isSeekToBeginningConsume() {
        return isSeekToBeginningConsume;
    }

    public void setSeekToBeginningConsume(boolean seekToBeginningConsume) {
        isSeekToBeginningConsume = seekToBeginningConsume;
    }

    public boolean isSeekToEndConsume() {
        return isSeekToEndConsume;
    }

    public void setSeekToEndConsume(boolean seekToEndConsume) {
        isSeekToEndConsume = seekToEndConsume;
    }

    public boolean isSpecificAvroReader() {
        return specificAvroReader;
    }

    public void setSpecificAvroReader(boolean specificAvroReader) {
        this.specificAvroReader = specificAvroReader;
    }

    public String getConsumerPartitions() {
        return consumerPartitions;
    }

    public void setConsumerPartitions(String consumerPartitions) {
        this.consumerPartitions = consumerPartitions;
    }

    public String getConsumeDuration() {
        return consumeDuration;
    }

    public void setConsumeDuration(String consumeDuration) {
        this.consumeDuration = consumeDuration;
    }

    public ProducerRecord buildProducerRecord(Object key, Object payload) {
        if (key == null) {
            key = UUID.randomUUID().toString();
        }
        return new ProducerRecord(this.producerTopic, key, payload);
    }

    public boolean isProducerConfigured() {
        return GStringUtil.isNotEmpty(this.producerTopic);
    }

    public boolean isConsumerConfigured() {
        return GStringUtil.isNotEmpty(this.consumerTopic);
    }

    public String getSchemaRegisterUrl() {
        return schemaRegisterUrl;
    }

    public void setSchemaRegisterUrl(String schemaRegisterUrl) {
        this.schemaRegisterUrl = schemaRegisterUrl;
    }

    public Properties getProducerConfig() {
        Properties properties = new Properties();
        String saslJaasConfig = saslJaasConfigClass + " required username=\"" + username + "\" password=\"" + password + "\";";
        properties.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, this.bootstrapServers);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, this.keySerializer);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, this.valueSerializer);
        properties.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, this.securityProtocol);
        properties.put(CommonClientConfigs.CLIENT_ID_CONFIG, this.clientId);
        properties.put(SaslConfigs.SASL_JAAS_CONFIG, saslJaasConfig);
        properties.put(SaslConfigs.SASL_MECHANISM, this.saslMechanism);
        return properties;
    }

    public Properties getConsumerConfig() {
        Properties properties = new Properties();
        String saslJaasConfig = saslJaasConfigClass + " required username=\"" + username + "\" password=\"" + password + "\";";
        properties.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, this.bootstrapServers);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, this.keyDeserializer);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, this.valueDeserializer);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, this.groupId);
        properties.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, this.securityProtocol);
        properties.put(SaslConfigs.SASL_JAAS_CONFIG, saslJaasConfig);
        properties.put(SaslConfigs.SASL_MECHANISM, this.saslMechanism);
        return properties;
    }
}

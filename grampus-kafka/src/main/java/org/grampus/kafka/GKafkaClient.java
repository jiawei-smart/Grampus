package org.grampus.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.TopicPartition;
import org.grampus.core.client.GAPIBase;
import org.grampus.log.GLogger;
import org.grampus.util.GStringUtil;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GKafkaClient implements GAPIBase<GKafkaOptions> {
    private GKafkaOptions config;
    private GKafkaMsgHandler handler;
    private KafkaProducer producer;

    private List<KafkaConsumer> consumers = new ArrayList<>();
    private ExecutorService consumerExecutor;

    public GKafkaClient(GKafkaMsgHandler handler) {
        this.handler = handler;
    }

    @Override
    public boolean start(GKafkaOptions config) {
        boolean started = false;
        if(config == null){
            GLogger.error("kafka config is null");
            return started;
        }
        if(this.config.isProducerConfigured()){
            try{
                Properties properties = this.config.getProducerConfig();
                this.producer = new KafkaProducer(properties);
                started = true;
            }catch (Exception e){
                GLogger.error("failure to innit the kafka producer with, {}", e);
            }
        }

        if(this.config.isConsumerConfigured()){
            Properties properties = this.config.getConsumerConfig();
            if(GStringUtil.isEmpty(this.config.getConsumerPartitions())){
                KafkaConsumer consumer  = new KafkaConsumer(properties);
                consumer.subscribe(Collections.singleton(config.getConsumerTopic()));
                consumers.add(consumer);
            }else {
                for(String partitionGroupStr : GStringUtil.splitAsList(config.getConsumerPartitions(),";")){
                    List<TopicPartition> topicPartitions = new ArrayList<>();
                    for(String partitionId : GStringUtil.splitAsList(partitionGroupStr,",")){
                        topicPartitions.add(new TopicPartition(config.getConsumerTopic(),Integer.parseInt(partitionId)));
                    }
                    KafkaConsumer consumer = new KafkaConsumer(properties);
                    consumer.assign(topicPartitions);
                    if(config.isSeekToBeginningConsume()){
                        consumer.seekToBeginning(topicPartitions);
                    }else if(config.isSeekToEndConsume()){
                        consumer.seekToEnd(topicPartitions);
                    }else if(config.getStartSeq() >= 0){
                        topicPartitions.forEach(topicPartition -> consumer.seek(topicPartition,config.getStartSeq()));
                    }
                    this.consumers.add(consumer);
                }
            }
            consumerExecutor = Executors.newFixedThreadPool(consumers.size());
            consumers.forEach(consumer->{
                consumerExecutor.execute(()->{
                    Duration duration;
                    if(GStringUtil.isNotEmpty(config.getConsumeDuration())){
                        duration = Duration.parse(config.getConsumeDuration());
                    }else {
                        duration = Duration.ofMillis(config.getConsumeIntervalMills());
                    }
                    while (true){
                        ConsumerRecords records = consumer.poll(duration);
                        Iterator<ConsumerRecord> iterator = records.iterator();
                        while (iterator.hasNext()){
                            ConsumerRecord record = iterator.next();
                            if(record.value() != null && this.handler != null){
                                this.handler.onConsumerRecord(record);
                                GLogger.debug("KAFKA RECEIVED: [{}]",record);
                            }
                        }
                    }
                });
            });
            started =true;
        }
        return started;
    }

    @Override
    public boolean stop() {
        return false;
    }

    public void send(ProducerRecord record){
        if(this.producer != null){
            producer.send(record);
        }
    }

    public void send(Object key, Object payload){
        this.send(this.config.buildProducerRecord(key,payload));
    }


}

package org.grampus.core;

import org.grampus.core.message.GMessage;
import org.grampus.core.messagebus.GMessageConsumer;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class GAdaptor {
    private GRouter router;
    private String id;
    private Integer eventSeq;
    private String event;
    private String serviceName;

    private Map<String,Set<String>> nextPathsCache = new HashMap<>();

    public GAdaptor(String serviceName, String event, Integer eventSeq) {
        this(serviceName,event,eventSeq,null);
    }

    public GAdaptor(String serviceName, String event, Integer eventSeq, GRouter router) {
        this.eventSeq = eventSeq;
        this.event = event;
        this.serviceName = serviceName;
        if (eventSeq != null && eventSeq >= 0) {
            this.id = buildAdaptorId(this.serviceName,event,eventSeq);
        } else {
            this.id = buildAdaptorId(this.serviceName,event);
        }
        this.router = router;
    }

    public static String buildAdaptorId(String serviceName, String event, Integer eventSeq) {
        return serviceName + GConstant.CELL_ID_SPLIT_CHAR + event + GConstant.CELL_ID_SPLIT_CHAR + eventSeq;
    }

    public static String buildAdaptorId(String serviceName, String event) {
        return serviceName + GConstant.CELL_ID_SPLIT_CHAR + event;
    }

    public static String buildAdaptorId(GEvent event) {
        return event.getService() + GConstant.CELL_ID_SPLIT_CHAR + event.getEventStem();
    }

    public String getId() {
        return id;
    }

    public String getEvent() {
        return event;
    }

    public String getServiceName() {
        return serviceName;
    }

    public int getEventSeq() {
        return eventSeq;
    }


    public void publishMessage(String nextEvent, GMessage gMessage) {
        if(nextEvent == null){
            nextEvent = this.event;
        }
        Set<String> nextPaths = getNextEvents(nextEvent);
        if(nextPaths != null){
            gMessage.header.update(this.event, this.id);
            this.router.toMessageBus(nextPaths,gMessage);
        }
    }

    private Set<String> getNextEvents(String nextEvent) {
        if(nextPathsCache.containsKey(nextEvent)){
            return nextPathsCache.get(nextEvent);
        }else {
           Set<String> nextPaths = this.router.nextMessagePaths(this.serviceName, this.event, eventSeq, nextEvent);
           this.nextPathsCache.put(nextEvent,nextPaths);
           return nextPaths;
        }
    }

    public void consume(GMessageConsumer consumer) {
        this.consume(consumer, false);
    }

    public void consume(GMessageConsumer consumer, boolean isWork) {
        this.router.consume(this.id, consumer, isWork);
    }

    public void consume(String topic, GMessageConsumer consumer, boolean isWork) {
        this.router.consume(topic, consumer, isWork);
    }

    public void toMessageBus(String topic, GMessage message) {
        this.router.toMessageBus(topic, message);
    }

    public void setRouter(GRouter router) {
        this.router = router;
    }

    public void clearNextPathCache(){
        this.nextPathsCache.clear();
    }
}



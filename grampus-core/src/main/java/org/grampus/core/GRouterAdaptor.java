package org.grampus.core;

import org.grampus.core.message.GMessage;
import org.grampus.core.messagebus.GMessageConsumer;

public class GRouterAdaptor {
    private GRouter router;
    private String id;
    private int eventSeq;
    private String event;
    private String serviceName;

    public GRouterAdaptor(String serviceName, String event, int eventSeq) {
        this.eventSeq = eventSeq;
        this.event = event;
        this.serviceName = serviceName;
        this.id = serviceName+GConstant.CELL_ID_SPLIT_CHAR+event+GConstant.CELL_ID_SPLIT_CHAR+eventSeq;
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


    public void publishMessage(String nextEvent, Object message) {
       if(message instanceof GMessage){
           ((GMessage)message).header.update(this.event,this.id);
           this.router.publish(this,nextEvent, (GMessage) message);
       }else {
           GMessage gMessage = new GMessage(this.id);
           gMessage.setPayload(message);
           this.router.publish(this,nextEvent,gMessage);
       }
    }

    public void consume(GMessageConsumer consumer){
        this.consume(consumer,false);
    }

    public void consume(GMessageConsumer consumer, boolean isWork){
        this.router.consume(this.id,consumer,isWork);
    }

    public void toMessageBus(String topic, GMessage message) {
        this.router.toMessageBus(topic, message);
    }

    public void setRouter(GRouter router) {
        this.router = router;
    }
}



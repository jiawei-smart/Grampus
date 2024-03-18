package org.grampus.core.messagebus.imp;

import io.vertx.core.*;
import io.vertx.core.eventbus.Message;
import org.grampus.core.messagebus.GMessageBus;
import org.grampus.core.messagebus.GMessageConsumer;
import org.grampus.core.message.GMessage;
import org.grampus.core.message.GMessageCodec;
import org.grampus.log.GLogger;

public class GMessageBusImp implements GMessageBus {
    private final Vertx vertx;

    public GMessageBusImp() {
        VertxOptions options = new VertxOptions();
        vertx = Vertx.vertx(options);
        vertx.eventBus().registerDefaultCodec(GMessage.class, new GMessageCodec());
    }

    @Override
    public void consume(String topic, GMessageConsumer consumer, boolean isWorker) {
        DeploymentOptions deploymentOptions = new DeploymentOptions();
        deploymentOptions.setThreadingModel(isWorker ? ThreadingModel.WORKER : ThreadingModel.EVENT_LOOP);
        vertx.deployVerticle(new AbstractVerticle() {
            @Override
            public void start() throws Exception {
                getVertx().eventBus().consumer(topic, new Handler<Message<GMessage>>() {
                    @Override
                    public void handle(Message<GMessage> message) {
                        try {
                            consumer.handle(message.body());
                        }catch (Exception exception){
                            GLogger.error("failure handle message {} for topic :{}, with {}",message.body(),topic, exception);
                        }
                    }
                });
            }
        }, deploymentOptions);
    }

    @Override
    public void publish(String topic, GMessage message) {
        vertx.eventBus().publish(topic, message);
    }
}

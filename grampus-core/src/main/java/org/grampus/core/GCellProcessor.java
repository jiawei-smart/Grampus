package org.grampus.core;

import io.netty.channel.EventLoop;
import org.grampus.core.message.GMessage;
import org.grampus.core.messagebus.GMessageConsumer;

import java.util.concurrent.atomic.AtomicBoolean;

public class GCellProcessor {
    private EventLoop executor;
    private GMessageConsumer messageConsumer;

    public GCellProcessor(EventLoop executor, GMessageConsumer messageConsumer) {
        this.executor = executor;
        this.messageConsumer = messageConsumer;
    }


    public void acceptMessage(GMessage message){
       this.executor.execute(()->this.messageConsumer.handle(message));
    }

}

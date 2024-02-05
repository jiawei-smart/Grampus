package org.grampus.core;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import org.grampus.core.eventbus.GMessageBus;
import org.grampus.core.eventbus.imp.GMessageBusImp;

public class  GContext {
    private EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
    private GMessageBus messageBus = new GMessageBusImp();
}

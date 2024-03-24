package org.grampus.core;

import io.netty.channel.EventLoop;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.vertx.core.impl.cpu.CpuCoreSensor;
import net.openhft.affinity.AffinityStrategies;
import net.openhft.affinity.AffinityThreadFactory;
import org.grampus.core.util.GYaml;
import org.grampus.log.GLogger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class GContext {
//    public static final int DEFAULT_EVENT_LOOP_POOL_SIZE = CpuCoreSensor.availableProcessors();
//    private EventLoopGroup executorService = new NioEventLoopGroup(DEFAULT_EVENT_LOOP_POOL_SIZE,
//            new AffinityThreadFactory("Grampus-eventLoop", AffinityStrategies.SAME_CORE));
//

    private Map<String, Object> workflowConfig;
    public final GRouter router = new GRouter();

    private GTester tester;


    public GContext(String configYaml) {
        Map config = GYaml.load(configYaml);
        if(config != null){
            this.workflowConfig = config;
        }else {
            this.workflowConfig = new HashMap();
        }
    }

    public void start(Map<String, GService> services, List<String> chains) {
        router.parseWorkflowChain(chains);
        router.parseServiceEventChain(services);
        services.values().forEach(service->{
            service.initService();
            service.initCells();
        });
        services.values().forEach(service->service.startCells());
        if(this.tester != null){
            GLogger.info("==Grampus start test model==");
            this.tester.start();
        }
    }

//    public Future submitTask(Runnable runnable){
//        return this.executorService.submit(runnable);
//    }

    public Map<Object, Object> getServiceConfig(String name) {
        return (Map<Object, Object>) workflowConfig.get(name);
    }

    public Map<String, Object> getGlobalConfig() {
        return this.workflowConfig;
    }

    public void setTester(GTester gTester) {
        this.tester = gTester;
    }

    public void addAssertTask(Runnable runnable) {
        if(this.tester != null){
            this.tester.addAssertTask(runnable);
        }else {
            GLogger.error("the asserts only can be used in test model!");
        }
    }
}

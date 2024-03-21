package org.grampus.core;

import io.netty.channel.EventLoop;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import net.openhft.affinity.AffinityStrategies;
import net.openhft.affinity.AffinityThreadFactory;
import org.grampus.core.util.GYaml;
import org.grampus.log.GLogger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class GContext {

    private EventLoopGroup executorService = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors(),
            new AffinityThreadFactory("g-eventLoop", AffinityStrategies.SAME_CORE));//,new AffinityThreadFactory("g-eventLoop", AffinityStrategies.DIFFERENT_CORE)
    private EventLoopGroup executorBlockingService = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors());

    private Map workflowConfig;
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


    protected void addChain(String chainStr){

    }

    public void start(Map<String, GService> services, List<String> chains) {
        router.parseWorkflowChain(chains);
        router.parseServiceEventChain(services);
        services.values().forEach(service->{
            service.initService();
            service.initCells();
        });
        if(this.tester != null){
            GLogger.info("==Grampus start test model==");
            this.tester.start();
        }
    }

    public Future submitTask(Runnable runnable){
        return this.executorService.submit(runnable);
    }

    public Future submitTask(Callable callable){
        return this.executorService.submit(callable);
    }

    public Future submitBlockingTask(Runnable runnable){
        return this.executorBlockingService.submit(runnable);
    }

    public Future submitBlockingTask(Callable callable){
        return this.executorBlockingService.submit(callable);
    }



    public Map<Object, Object> getServiceConfig(String name) {
        return (Map<Object, Object>) workflowConfig.get(name);
    }

    public Map<Object, Object> getGlobalConfig() {
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

    public EventLoop nextTaskExecutor() {
        return this.executorService.next();
    }
    public EventLoop nextBlockingTaskExecutor() {
        return this.executorBlockingService.next();
    }

    public ScheduledFuture schedule(Runnable task, Long timer, TimeUnit timeUnit){
       return this.executorBlockingService.scheduleAtFixedRate(task,0,timer,timeUnit);
    }

}

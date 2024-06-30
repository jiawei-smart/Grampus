package org.grampus.core;

import io.netty.channel.EventLoop;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import org.grampus.core.annotation.config.GValueSpec;
import org.grampus.core.annotation.plugin.GPluginApi;
import org.grampus.core.annotation.plugin.GPluginAutowired;
import org.grampus.core.environment.DefaultGEnvironment;
import org.grampus.core.executor.GThreadChecker;
import org.grampus.core.executor.GThreadFactory;
import org.grampus.core.executor.GWorkerExecutor;
import org.grampus.core.monitor.GMonitorSupervisor;
import org.grampus.core.plugin.GPluginMessage;
import org.grampus.log.GLogger;
import org.grampus.util.GFileUtil;
import org.grampus.util.GYamlUtil;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.Collectors;

public class GContext {

    private GWorkflowOptions options;
    public final GRouter router;
    private final GMonitorSupervisor servicesSupervisor = new GMonitorSupervisor();
    private final GMonitorSupervisor cellsSupervisor = new GMonitorSupervisor();
    private GTester tester;
    private GThreadFactory threadFactory;
    private GWorkerExecutor workerExecutor;
    private ScheduledExecutorService scheduledExecutor;
    private Executor blockingExecutor;
    private GThreadChecker threadChecker;
    private Set<GCell> registeredCells = new HashSet<>();
    private Collection<GService> registeredServices;
    private GValueSpec gValueSpec;

    public GContext(String configYaml) {
        if (GFileUtil.isExistedInClasspath(configYaml)) {
            options = GYamlUtil.load(configYaml,GWorkflowOptions.class);
        }else {
            GLogger.warn("can not load config.yaml for [{}], will used default values",configYaml);
        }
        if (options == null) {
            this.options = new GWorkflowOptions();
        }
        gValueSpec = new GValueSpec(options.config());
        router = new GRouter(options);
        threadChecker = new GThreadChecker(options.getThreadCheckInterval(),options.getThreadCheckTimeUnit(),options.getThreadProcessTimeout(),options.getThreadProcessTimeoutTimeUnit());
        threadFactory =this.getGThreadFactory();
        EventLoopGroup workPool = new NioEventLoopGroup(options.getWorkerPoolSize(), threadFactory);
        workerExecutor = new GWorkerExecutor(workPool);
        scheduledExecutor = Executors.newScheduledThreadPool(options.getScheduledTheadPoolSize());
        blockingExecutor = !threadFactory.isSupportVThread() ? Executors.newCachedThreadPool() : (runnable)->threadFactory.newVirtualThread(runnable).start();
        Runtime.getRuntime().addShutdownHook(new Thread(()->this.close()));
    }

    public void start(Collection<GService> services, List<String> chains) {
        registeredServices =  services;
        router.parseWorkflowChain(chains);
        initServicesSupervisor(services);
        gValueSpec.enrich(services);
        servicesInit(services);
        cellsInit(services);
        initCellsSupervisor(services);
        router.parseServiceEventChain(services);
        servicesStart(services);
        servicesCellsBeforeStart(services);
        servicesCellsStart(services);
        this.servicesSupervisor.startUpdateMonitorMap(scheduledExecutor);
        this.cellsSupervisor.startUpdateMonitorMap(scheduledExecutor);
        if (this.tester != null) {
            GLogger.info(GConstant.GRAMPUS_TEST_LOGO);
            this.tester.start();
        } else {
            printInfo(services);
        }
    }

    private void cellsInit(Collection<GService> services) {
        services.forEach(service->{
            registeredCells.addAll(service.getEvents().values().stream().flatMap(gEvent -> gEvent.handler().getCells().stream()).collect(Collectors.toSet()));
        });
        for (GCell cell : registeredCells) {
            Field[] fields = cell.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(GPluginAutowired.class)) {
                    if (field.getType().isAnnotationPresent(GPluginApi.class)) {
                        injectHandlerProxy(cell, field);
                    } else {
                        GLogger.warn(" the GPlugin autowired type [{}] un-accepted non-GPluginApi class", field.getType());
                    }
                }
            }
            gValueSpec.enrich(cell);
        }
    }

    public Executor getSingleExecutor(){
        EventLoop executor = workerExecutor.getExecutor();
        return new Executor(){
            @Override
            public void execute(Runnable command) {
                workerExecutor.execute(command,executor);
            }
        };
    }

    private void injectHandlerProxy(GCell cell, Field field) {
        String pluginEvent = field.getType().getAnnotation(GPluginApi.class).event();
        Object proxy = Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{field.getType()}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                cell.onPlugin(pluginEvent, new GPluginMessage(method,args));
                return null;
            }
        });
        field.setAccessible(true);
        try {
            field.set(cell, proxy);
        } catch (IllegalAccessException e) {
            GLogger.warn("failure to inject GPluginApi for [{}]", field.getType());
        }
    }

    private void servicesCellsBeforeStart(Collection<GService> services) {
        services.forEach(GService::cellsBeforeStart);
    }

    private void servicesCellsStart(Collection<GService> services) {
        services.forEach(GService::startCells);
    }

    private void servicesStart(Collection<GService> services) {
        services.forEach(service -> {
            service.start();
        });
        registeredCells.forEach(cell -> {
            this.cellsSupervisor.broadcast(cell.getId(), cell.monitorMap());
        });
    }

    private void initCellsSupervisor(Collection<GService> services) {
        registeredCells.forEach(cell -> {
            this.cellsSupervisor.addMonitor(cell);
        });
    }

    private void servicesInit(Collection<GService> services) {
        services.forEach(service -> {
            service.init();
            this.servicesSupervisor.broadcast(service.getName(), service.monitorMap());
        });
    }

    private void initServicesSupervisor(Collection<GService> services) {
        services.forEach(service -> this.servicesSupervisor.addMonitor(service));
    }

    private void printInfo(Collection<GService> services) {
        GLogger.info(GConstant.GRAMPUS_LOGO);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n========================== Workflow Info =============================\n\n");
        stringBuilder.append("## SERVICE: \n");
        services.forEach((gService) -> {
            stringBuilder.append("   -*- service: [" + gService.getName() + "]").append("\n");
            gService.getEvents().values().forEach((gEvent) -> {
                stringBuilder.append("       -- event: " + gEvent.getEventStem()).append("  >>  cells: [");
                gEvent.handler().getCells().forEach(cell -> {
                    stringBuilder.append(cell.getId()).append(", ");
                });
                stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
                stringBuilder.append("]\n");
            });
        });
        stringBuilder.append("\n## CHAINS: \n");
        this.router.getChainsEventTable().forEach((sourceEvent, targetEvents) -> {
            stringBuilder.append("   -*- " + sourceEvent + " => [");
            targetEvents.forEach(event -> stringBuilder.append(event + ", "));
            stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
            stringBuilder.append("]\n");
        });
        stringBuilder.append("\n=====================================================================\n");
        GLogger.info(stringBuilder.toString());
    }

    public Map<Object, Object> getServiceConfig(String name) {
        return (Map<Object, Object>) options.configService(name);
    }

    public Map<String, Object> getGlobalConfig() {
        return this.options.config();
    }

    public void setTester(GTester gTester) {
        this.tester = gTester;
    }

    public void addAssertTask(Runnable runnable) {
        if (this.tester != null) {
            this.tester.addAssertTask(runnable);
        } else {
            GLogger.error("the asserts only can be used in test model!");
        }
    }

    public GThreadFactory getGThreadFactory() {
        if(threadFactory == null){
            threadFactory = new GThreadFactory(threadChecker);
        }
        return threadFactory;
    }

    public GWorkerExecutor getWorkerExecutor() {
        return workerExecutor;
    }

    public ScheduledExecutorService getScheduledExecutor() {
        return scheduledExecutor;
    }

    public Executor getBlockingExecutor() {
        return blockingExecutor;
    }

    public void close(){
        registeredCells.forEach(cell -> cell.close());
        servicesSupervisor.close();
        cellsSupervisor.close();
        scheduledExecutor.shutdownNow();
        registeredServices.forEach(GService::stop);
    }
}

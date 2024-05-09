package org.grampus.core;

import org.grampus.core.annotation.plugin.GPluginApi;
import org.grampus.core.annotation.plugin.GPluginAutowired;
import org.grampus.core.monitor.GMonitorSupervisor;
import org.grampus.core.plugin.GPluginMessage;
import org.grampus.log.GLogger;
import org.grampus.util.GFileUtil;
import org.grampus.util.GYamlUtil;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GContext {

    private Map<String, Object> workflowConfig;
    public final GRouter router = new GRouter();

    private final GMonitorSupervisor servicesSupervisor = new GMonitorSupervisor();
    private final GMonitorSupervisor cellsSupervisor = new GMonitorSupervisor();
    private GTester tester;


    public GContext(String configYaml) {
        Map config = null;
        if (GFileUtil.isExistedInClasspath(configYaml)) {
            config = GYamlUtil.load(configYaml);
        }
        if (config != null) {
            this.workflowConfig = config;
        } else {
            this.workflowConfig = new HashMap();
        }
    }

    public void start(Collection<GService> services, List<String> chains) {
        router.parseWorkflowChain(chains);
        initServicesSupervisor(services);
        servicesBeforeStart(services);
        initCellsSupervisor(services);
        router.parseServiceEventChain(services);
        servicesStart(services);
        processCellsPluginAnnotation(services);
        servicesCellsBeforeStart(services);
        servicesCellsStart(services);
        this.servicesSupervisor.startUpdateMonitorMap();
        this.cellsSupervisor.startUpdateMonitorMap();
        if (this.tester != null) {
            GLogger.info(GConstant.GRAMPUS_TEST_LOGO);
            this.tester.start();
        } else {
            printInfo(services);
        }
    }

    private void processCellsPluginAnnotation(Collection<GService> services) {
        for (GService service : services) {
            List<GCell> cells = service.getCells().values().stream().flatMap(Collection::stream).collect(Collectors.toList());
            for (GCell cell : cells) {
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
            }
        }
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
            service.getCells().forEach((gEvent, cells) -> {
                cells.forEach(cell -> this.cellsSupervisor.broadcast(cell.getId(), cell.monitorMap()));
            });
        });
    }

    private void initCellsSupervisor(Collection<GService> services) {
        services.forEach(service -> {
            service.getCells().forEach((gEvent, cells) -> {
                cells.forEach(cell -> this.cellsSupervisor.addMonitor(cell));
            });
        });
    }

    private void servicesBeforeStart(Collection<GService> services) {
        services.forEach(service -> {
            service.beforeStart();
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
            gService.getCells().forEach((event, gCells) -> {
                stringBuilder.append("       -- event: " + event.getEventStem()).append("  >>  cells: [");
                gCells.forEach(cell -> {
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
        return (Map<Object, Object>) workflowConfig.get(name);
    }

    public Map<String, Object> getGlobalConfig() {
        return this.workflowConfig;
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
}

package org.grampus.core;

import org.grampus.core.monitor.GMonitorSupervisor;
import org.grampus.util.GYamlUtil;
import org.grampus.log.GLogger;

import java.util.*;

public class GContext {

    private Map<String, Object> workflowConfig;
    public final GRouter router = new GRouter();

    private final GMonitorSupervisor servicesSupervisor = new GMonitorSupervisor();
    private final GMonitorSupervisor cellsSupervisor = new GMonitorSupervisor();
    private GTester tester;


    public GContext(String configYaml) {
        Map config = GYamlUtil.load(configYaml);
        if(config != null){
            this.workflowConfig = config;
        }else {
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
        servicesCellsBeforeStart(services);
        servicesCellsStart(services);
        this.servicesSupervisor.startUpdateMonitorMap();
        this.cellsSupervisor.startUpdateMonitorMap();
        if(this.tester != null){
            GLogger.info(GConstant.GRAMPUS_TEST_LOGO);
            this.tester.start();
        }else {
            printInfo(services);
        }
    }

    private void servicesCellsBeforeStart(Collection<GService> services) {
        services.forEach(GService::cellsBeforeStart);
    }

    private void servicesCellsStart(Collection<GService> services) {
        services.forEach(GService::startCells);
    }

    private void servicesStart(Collection<GService> services) {
        services.forEach(service->{
            service.start();
            service.getCells().forEach((gEvent,cells)->{
                cells.forEach(cell->this.cellsSupervisor.broadcast(cell.getId(),cell.monitorMap()));
            });
        });
    }

    private void initCellsSupervisor(Collection<GService> services) {
        services.forEach(service->{
            service.getCells().forEach((gEvent, cells)->{
                cells.forEach(cell->this.cellsSupervisor.addMonitor(cell));
            });
        });
    }

    private void servicesBeforeStart(Collection<GService> services) {
        services.forEach(service->{
            service.beforeStart();
            this.servicesSupervisor.broadcast(service.getName(), service.monitorMap());
        });
    }

    private void initServicesSupervisor(Collection<GService> services) {
        services.forEach(service->this.servicesSupervisor.addMonitor(service));
    }

    private void printInfo(Collection<GService> services) {
        GLogger.info(GConstant.GRAMPUS_LOGO);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n========================== Workflow Info =============================\n\n");
        stringBuilder.append("## SERVICE: \n");
        services.forEach((gService) -> {
            stringBuilder.append("   -*- service: ["+gService.getName()+"]").append("\n");
            gService.getCells().forEach((event, gCells) -> {
                stringBuilder.append("       -- event: "+event.getEventStem()).append("  >>  cells: [");
                gCells.forEach(cell->{
                    stringBuilder.append(cell.getId()).append(", ");
                });
                stringBuilder.delete(stringBuilder.length()-2,stringBuilder.length());
                stringBuilder.append("]\n");
            });
        });
        stringBuilder.append("\n## CHAINS: \n");
        this.router.getChainsEventTable().forEach((sourceEvent,targetEvents)->{
            stringBuilder.append("   -*- "+sourceEvent+" => [");
            targetEvents.forEach(event->stringBuilder.append(event+", "));
            stringBuilder.delete(stringBuilder.length()-2,stringBuilder.length());
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
        if(this.tester != null){
            this.tester.addAssertTask(runnable);
        }else {
            GLogger.error("the asserts only can be used in test model!");
        }
    }
}

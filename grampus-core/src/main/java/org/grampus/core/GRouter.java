package org.grampus.core;

import org.grampus.core.util.GStringUtil;

import javax.print.DocFlavor;
import java.util.*;
import java.util.stream.Collectors;

public class GRouter {
    private Map<String, Map<GEvent, Map<String, String>>> serviceEventPaths = new HashMap<>();
    private Map<GEvent, List<GEvent>> workflowEventPaths = new HashMap<>();

    public void registerWorkflowEventPath(List<String> chainStrList) {
        chainStrList.forEach(chainStr -> {
            List<GEvent> events = parseChainStrAsEvents(chainStr);
            for (int i = 0; i < events.size(); i++) {
                if (i + 1 < events.size()) {
                    registerEventPath(events.get(i), events.get(i + 1));
                }
            }
        });
    }

    private void registerEventPath(GEvent start, GEvent end) {
        if (workflowEventPaths.containsKey(start)) {
            List<GEvent> eventPath = workflowEventPaths.get(start);
            eventPath.add(end);
        } else {
            List<GEvent> eventPath = new ArrayList<>();
            eventPath.add(end);
            workflowEventPaths.put(start, eventPath);
        }
    }

    private List<GEvent> parseChainStrAsEvents(String chainStr) {
        List<String> eventStrList = Arrays.asList(GStringUtil.split(chainStr, "->"));
        return eventStrList.stream().map(eventStr -> new GEvent(eventStr)).collect(Collectors.toList());
    }

    public void registerServiceEventPath(Map<String, GService> services) {
        services.values().forEach(service -> {
            Map<String, List<GCell>> eventCells = service.getCells();
            Map<GEvent, Map<String, String>> serviceEventCellPath = new HashMap<>();
            eventCells.keySet().forEach(eventStr -> {
                GEvent event = new GEvent(service.getName(), eventStr);
                List<GCell> cells = eventCells.get(eventStr);
                Map<String, String> eventCellPath = new HashMap<>();
                eventCellPath.put(GConstant.EVENT_FIRST_CELL_ID, cells.get(0).getId());
                for (int i = 0; i < cells.size(); i++) {
                    if (i + 1 < cells.size()) {
                        eventCellPath.put(cells.get(i).getId(), cells.get(i + 1).getId());
                    }
                }
                serviceEventCellPath.put(event, eventCellPath);
            });
            this.serviceEventPaths.put(service.getName(), serviceEventCellPath);
        });
    }

    public String getNextPathValue(GEvent event, String cellId) {
        if (workflowEventPaths.containsKey(event)) {
            return workflowEventPaths.get(event).toString();
        } else {
            Map<GEvent, Map<String, String>> serviceEventPath = this.serviceEventPaths.get(event.getService());
            Map<String, String> eventPath = serviceEventPath.get(event);
            if (GStringUtil.isNotEmpty(cellId) && eventPath.containsKey(cellId)) {
                return eventPath.get(cellId);
            }
            return null;
        }
    }

    public String getNextPathValue(String serviceName, String eventStr, String cellId) {
        GEvent event = new GEvent(serviceName, eventStr);
        if (workflowEventPaths.containsKey(event)) {
            return workflowEventPaths.get(event).toString();
        } else {
            Map<GEvent, Map<String, String>> serviceEventPath = this.serviceEventPaths.get(event.getService());
            Map<String, String> eventPath = serviceEventPath.get(event);
            if (GStringUtil.isNotEmpty(cellId) && eventPath.containsKey(cellId)) {
                return eventPath.get(cellId);
            }
            return null;
        }
    }
    //foreach from 1 to 100
}

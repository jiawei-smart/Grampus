package org.grampus.core;

import org.grampus.core.message.GMessage;
import org.grampus.core.messagebus.GMessageBus;
import org.grampus.core.messagebus.GMessageConsumer;
import org.grampus.core.messagebus.imp.GMessageBusImp;
import org.grampus.core.util.GStringUtil;

import java.util.*;

public class GRouter {
    public final GMessageBus messageBus = new GMessageBusImp();
    private Map<String, Map<String, Map<Integer, String>>> servicesEventCellTable = new HashMap<>();
    private Map<String, Set<String>> chainsEventTable = new HashMap<>();

    private Map<String, Set<String>> serviceOpenEvents = new HashMap<>();

    private Map<String, GAdaptor> registeredAdaptors = new HashMap<>();

    public void parseWorkflowChain(List<String> chainStrList) {
        chainStrList.forEach(chainStr -> {
            List<String> events = parseChainStrAsEvents(chainStr);
            for (int i = 0; i < events.size(); i++) {
                if (i + 1 < events.size()) {
                    addChainEventPath(events.get(i), events.get(i + 1));
                }
            }
        });
    }

    private void addChainEventPath(String source, String target) {
        String sourceEventAdaptorId = GAdaptor.buildAdaptorId(new GEvent(source));
        String targetEventAdaptorId = GAdaptor.buildAdaptorId(new GEvent(target));

        if (chainsEventTable.containsKey(sourceEventAdaptorId)) {
            Set<String> targets = chainsEventTable.get(sourceEventAdaptorId);
            targets.add(targetEventAdaptorId);
        } else {
            Set<String> targets = new HashSet<>();
            targets.add(targetEventAdaptorId);
            chainsEventTable.put(sourceEventAdaptorId, targets);
        }
    }

    private List<String> parseChainStrAsEvents(String chainStr) {
        return Arrays.asList(GStringUtil.split(chainStr, GConstant.CHAIN_SPLIT_CHAR));
    }

    public void parseServiceEventChain(Map<String, GService> services) {
        services.values().forEach(service -> {
            Map<GEvent, List<GCell>> eventCells = service.getCells();
            Map<String, Map<Integer, String>> serviceEventListenerPath = new HashMap<>();
            eventCells.keySet().forEach(event -> {
                Map<Integer, String> eventListenerTable = new HashMap<>();
                GAdaptor eventListenerAdaptor = registerAdaptor(event);
                event.initDefaultEventListener(eventListenerAdaptor);
                eventListenerTable.put(-1,eventListenerAdaptor.getId());
                List<GCell> cells = eventCells.get(event);
                for (int i = 0; i < cells.size(); i++) {
                    GAdaptor cellAdaptor = registerAdaptor(service.getName(), event.getEventStem(), i);
                    cells.get(i).setAdaptor(cellAdaptor);
                    eventListenerTable.put(i, cellAdaptor.getId());
                }
                serviceEventListenerPath.put(event.getEventStem(), eventListenerTable);
            });
            this.servicesEventCellTable.put(service.getName(), serviceEventListenerPath);
        });
    }

    private GAdaptor registerAdaptor(GEvent event) {
        return registerAdaptor(event.getService(),event.getEventStem(),-1);
    }

    private GAdaptor registerAdaptor(String service, String eventStem, Integer eventSeq) {
        GAdaptor adaptor = new GAdaptor(service, eventStem, eventSeq, this);
        this.registeredAdaptors.put(adaptor.getId(),adaptor);
        return adaptor;
    }

    private Set<String> nextMessagePathFromWorkflowChain(String serviceName, String currentEvent, String targetEvent) {
        if (isOpenEvent(serviceName, targetEvent)){
            String adapterId = GAdaptor.buildAdaptorId(serviceName,targetEvent);
            if (chainsEventTable.containsKey(adapterId)) {
                return chainsEventTable.get(adapterId);
            }
        }
        return Collections.EMPTY_SET;
    }

    private String nextMessagePathFromSameService(String serviceName, String currentEvent, Integer currentEventSeq, String targetEvent) {
        if (this.servicesEventCellTable.containsKey(serviceName)) {
            Map<String, Map<Integer, String>> servicesEventCellTable = this.servicesEventCellTable.get(serviceName);
            if(GStringUtil.equals(currentEvent,targetEvent)){
                Map<Integer, String> eventPath = servicesEventCellTable.get(currentEvent);
                Integer nextEventSeq = currentEventSeq + 1;
                if (eventPath.containsKey(nextEventSeq)) {
                    return eventPath.get(nextEventSeq);
                }
            }else if(servicesEventCellTable.containsKey(targetEvent)){
                return servicesEventCellTable.get(targetEvent).get(-1);
            }
        }
        return null;
    }

    public Set<String> nextMessagePaths(String serviceName, String currentEvent, Integer currentEventSeq,String targetEvent) {
        String nextPath = this.nextMessagePathFromSameService(serviceName, currentEvent, currentEventSeq, targetEvent);
        if(nextPath == null){
            return nextMessagePathFromWorkflowChain(serviceName, currentEvent, targetEvent);
        }else {
            Set<String> nextPaths = new HashSet();
            nextPaths.add(nextPath);
            return nextPaths;
        }
    }

    private boolean isOpenEvent(String serviceName, String eventStr) {
        return (this.serviceOpenEvents.containsKey(serviceName)
                && this.serviceOpenEvents.get(serviceName).contains(eventStr))
                || GStringUtil.equals(eventStr, GConstant.GLOBAL_OPEN_ALL_EVENT);
    }

    public void consume(String topic, GMessageConsumer consumer, boolean isWorker) {
        this.messageBus.consume(topic, consumer);
    }

    public void toMessageBus(String topic, GMessage message) {
        this.messageBus.publish(topic, message);
    }

    public void toMessageBus(Set<String> nextPaths, GMessage message) {
        if (nextPaths != null) {
            nextPaths.forEach(path -> {
                toMessageBus(path, message);
            });
        }
    }

    public void addGlobalEvent(String service, String event) {
        if(!this.serviceOpenEvents.containsKey(service)){
            serviceOpenEvents.put(service, new HashSet<>());
        }
        this.serviceOpenEvents.get(service).add(event);
    }

    public void clearPathCache(){
        this.registeredAdaptors.values().forEach(GAdaptor::clearNextPathCache);
    }
}

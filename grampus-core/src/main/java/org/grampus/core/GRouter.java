package org.grampus.core;

import org.grampus.core.message.GMessage;
import org.grampus.core.messagebus.GMessageBus;
import org.grampus.core.messagebus.GMessageConsumer;
import org.grampus.core.messagebus.imp.GMessageBusImp;
import org.grampus.core.util.GStringUtil;

import java.util.*;

public class GRouter {
    public final GMessageBus messageBus = new GMessageBusImp();
    private Map<String, Map<String, Map<Integer, String>>> servicesEventPaths = new HashMap<>();
    private Map<String, Set<String>> chainsEventPaths = new HashMap<>();

    private Map<String, Set<String>> serviceOpenEvents = new HashMap<>();

    public void registerChainsEventPath(List<String> chainStrList) {
        chainStrList.forEach(chainStr -> {
            List<String> events = parseChainStrAsEvents(chainStr);
            for (int i = 0; i < events.size(); i++) {
                if (i + 1 < events.size()) {
                    registerEventPath(events.get(i), events.get(i + 1));
                }
            }
        });
    }

    private void registerEventPath(String source, String target) {
        String formatTarget = new GEvent(target).toString();
        if (chainsEventPaths.containsKey(source)) {
            Set<String> targets = chainsEventPaths.get(source);
            targets.add(formatTarget);
        } else {
            Set<String> targets = new HashSet<>();
            targets.add(formatTarget);
            chainsEventPaths.put(new GEvent(source).toString(), targets);
        }
    }

    private List<String> parseChainStrAsEvents(String chainStr) {
        return Arrays.asList(GStringUtil.split(chainStr, GConstant.CHAIN_SPLIT_CHAR));
    }

    public void registerServiceEventPath(Map<String, GService> services) {
        services.values().forEach(service -> {
            Map<String, List<GCell>> eventCells = service.getCells();
            Map<String, Map<Integer, String>> serviceEventCellPath = new HashMap<>();
            eventCells.keySet().forEach(eventStr -> {
                List<GCell> cells = eventCells.get(eventStr);
                Map<Integer, String> eventCellPath = new HashMap<>();
                for (int i = 0; i < cells.size(); i++) {
                    GRouterAdaptor adaptor = new GRouterAdaptor(service.getName(), eventStr, i);
                    adaptor.setRouter(this);
                    cells.get(i).setAdaptor(adaptor);
                    eventCellPath.put(i, adaptor.getId());
                }
                serviceEventCellPath.put(eventStr, eventCellPath);
            });
            this.servicesEventPaths.put(service.getName(), serviceEventCellPath);
        });
    }

    private Set<String> getChainNextEvent(String serviceName, String currentEvent, String eventStr) {
        String globalEventStr = serviceName + GConstant.EVENT_SPLIT_CHAR + currentEvent;
        if (chainsEventPaths.containsKey(globalEventStr)) {
            return chainsEventPaths.get(globalEventStr);
        }
        return null;
    }

    public String getServiceNextPathValue(String serviceName, String currentEvent, Integer currentEventSeq,String targetEvent) {
        Map<String, Map<Integer, String>> serviceEventPath = this.servicesEventPaths.get(serviceName);
        if (serviceEventPath != null ) {
            if(GStringUtil.equals(currentEvent,targetEvent)){
                Map<Integer, String> eventPath = serviceEventPath.get(currentEvent);
                Integer nextEventSeq = currentEventSeq + 1;
                if (eventPath.containsKey(nextEventSeq)) {
                    return eventPath.get(nextEventSeq);
                }
            }else {
                Map<Integer, String> eventPath = serviceEventPath.get(targetEvent);
                if(eventPath != null && eventPath.size() > 0 && eventPath.containsKey(0)){
                    return eventPath.get(0);
                }
            }
        }
        return null;
    }

    public String getServiceNextPathValue(String serviceName, String eventStr) {
        return this.getServiceNextPathValue(serviceName, eventStr,-1, eventStr);
    }

    public Set<String> nextMessagePaths(String serviceName, String targetEvent) {
        return this.nextMessagePaths(serviceName, targetEvent, -1, targetEvent);
    }

    public Set<String> nextMessagePaths(String serviceName, String currentEvent, Integer currentEventSeq,String targetEvent) {
        String nextPath = this.getServiceNextPathValue(serviceName, currentEvent, currentEventSeq, targetEvent);
        if (GStringUtil.isNotEmpty(nextPath)) {
            Set<String> nextPaths = new HashSet();
            nextPaths.add(nextPath);
            return nextPaths;
        } else if (isOpenEvent(serviceName, targetEvent)) {
            return getChainNextEvent(serviceName, currentEvent, targetEvent);
        } else {
            return null;
        }
    }

    private boolean isOpenEvent(String serviceName, String eventStr) {
        return (this.serviceOpenEvents.containsKey(serviceName)
                && this.serviceOpenEvents.get(serviceName).contains(eventStr))
                || GStringUtil.equals(eventStr, GConstant.GLOBAL_OPEN_EVENT);
    }

    public void consume(String topic, GMessageConsumer consumer, boolean isWorker) {
        this.messageBus.consume(topic, consumer);
    }

    public void publish(GRouterAdaptor adaptor, String nextEvent, GMessage message) {
        if (GStringUtil.isEmpty(nextEvent)) {
            nextEvent = adaptor.getEvent();
        }
        Set<String> nextPaths = nextMessagePaths(adaptor.getServiceName(), adaptor.getEvent(),  adaptor.getEventSeq(),nextEvent);
        toMessageBus(nextPaths, message);
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
}

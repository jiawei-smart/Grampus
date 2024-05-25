package org.grampus.core.customized;

import org.grampus.core.GCell;

import java.util.Map;
import java.util.Set;

public class Dispatcher extends GCell {
    private final Set<String> targetEvents;

    public Dispatcher(Set<String> targetEvents) {
        this.targetEvents = targetEvents;
    }

    @Override
    public void handle(Object payload, Map meta) {
        targetEvents.forEach(event -> {
            onEvent(event, payload, meta);
        });
    }
}

package org.grampus.core.customized;

import org.grampus.core.GCell;
import org.grampus.core.message.GMessage;

import java.util.Map;
import java.util.function.BiFunction;

public class Router<T,C> extends GCell<T> {
    private final BiFunction<T, Map, C> predictorCondition;
    private final Map<C, String> evenConditiontMap;

    public Router(BiFunction<T, Map, C> predictorCondition, Map<C, String> map) {
        this.predictorCondition = predictorCondition;
        this.evenConditiontMap = map;
    }

    @Override
    protected void handle(GMessage message) {
        C predictResult = predictorCondition.apply((T) message.payload(),message.meta());
        if (evenConditiontMap.containsKey(predictResult)) {
            redirectEvent(evenConditiontMap.get(predictResult),message);
        }
    }
}

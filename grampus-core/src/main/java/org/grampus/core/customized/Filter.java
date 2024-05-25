package org.grampus.core.customized;

import org.grampus.core.GCell;
import org.grampus.core.message.GMessageHeader;

import java.util.Map;
import java.util.function.BiPredicate;

public class Filter<T> extends GCell<T> {
    private final BiPredicate<T, Map> predictor;

    public Filter(BiPredicate<T, Map> predictor) {
        this.predictor = predictor;
    }

    @Override
    public Object handle(GMessageHeader header, T payload, Map meta) {
        if (predictor.test(payload, meta)) {
            return payload;
        } else {
            return null;
        }
    }
}

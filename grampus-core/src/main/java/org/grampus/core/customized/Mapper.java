package org.grampus.core.customized;

import org.grampus.core.GCell;
import org.grampus.core.message.GMessageHeader;

import java.util.Map;
import java.util.function.BiFunction;

public class Mapper<I,O> extends GCell<I> {
    private final BiFunction<I,Map,O> mapper;

    public Mapper(BiFunction<I,Map, O> mapper) {
        this.mapper = mapper;
    }

    @Override
    public Object handle(GMessageHeader header, I payload, Map meta) {
        return mapper.apply(payload, meta);
    }
}

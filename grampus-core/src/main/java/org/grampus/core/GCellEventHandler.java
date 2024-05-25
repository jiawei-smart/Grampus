package org.grampus.core;

import org.grampus.core.message.GMessageHeader;

import java.util.Map;

public interface GCellEventHandler<T> {
   Object handle(GMessageHeader header, T payload, Map meta);
}

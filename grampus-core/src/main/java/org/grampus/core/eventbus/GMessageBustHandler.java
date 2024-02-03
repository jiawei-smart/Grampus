package org.grampus.core.eventbus;

import org.grampus.core.message.GMessage;

public interface GMessageBustHandler {
    Object handle(GMessage message);
}

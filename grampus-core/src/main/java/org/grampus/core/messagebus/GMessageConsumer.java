package org.grampus.core.messagebus;

import org.grampus.core.message.GMessage;

public interface GMessageConsumer {
    void handle(GMessage message);
}

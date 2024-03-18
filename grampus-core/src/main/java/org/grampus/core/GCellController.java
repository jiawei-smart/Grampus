package org.grampus.core;

import org.grampus.core.message.GMessage;
import org.grampus.core.messagebus.GMessageConsumer;

public interface GCellController {
//   void publishMessage(String currentEvent, Integer eventSeq, String nextEvent, GMessage message);
//
//   void toMessageBus(String topic, GMessage message);
   void addTask(Runnable runnable);

   void addBlockingTask(Runnable runnable);

   default void consumeMessage(String topic, GMessageConsumer consumer){
      consumeMessage(topic, consumer,false);
   }

   void consumeMessage(String topic, GMessageConsumer consumer, boolean isWorker);

  String buildCellId(String event, int eventSeq);
}

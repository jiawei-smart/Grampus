package org.grampus.core;

import io.netty.channel.EventLoop;
import org.grampus.core.util.GYaml;

import java.util.concurrent.TimeUnit;

public interface GCellController {
   void addTask(Runnable runnable);

   GTimer createTimer(Runnable runnable);

   void addBlockingTask(Runnable runnable);

   <T> T getConfig(Object key, Class<T> type);

   default <T> T loadConfig(String fileName, Class<T> type){
      return GYaml.load(fileName, type);
   }

   void addAssertTask(Runnable runnable);

   EventLoop getTaskExecutor();
}

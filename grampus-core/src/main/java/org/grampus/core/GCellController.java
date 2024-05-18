package org.grampus.core;

import org.grampus.core.executor.GTimer;
import org.grampus.util.GYamlUtil;

import java.util.concurrent.Executor;

public interface GCellController {
   void submitBlockingTask(Runnable runnable);
   void submitTask(Runnable runnable);
   GTimer createTimer(Runnable runnable);
   <T> T getConfig(Object key, Class<T> type);
   default <T> T loadConfig(String fileName, Class<T> type){
      return GYamlUtil.load(fileName, type);
   }
   void submitAssertTask(Runnable runnable);

   Executor getSingleExecutor();
}

package org.grampus.core;

import org.grampus.core.util.GYaml;

public interface GCellController {
   void addTask(Runnable runnable);

   void addBlockingTask(Runnable runnable);

   <T> T getConfig(Object key, Class<T> type);

   default <T> T loadConfig(String fileName, Class<T> type){
      return GYaml.load(fileName, type);
   }
}

package org.grampus.core;

import org.grampus.util.GYamlUtil;

public interface GCellController {
   void addBlockingTask(Runnable runnable);
   GTimer createTimer(Runnable runnable);
   <T> T getConfig(Object key, Class<T> type);
   default <T> T loadConfig(String fileName, Class<T> type){
      return GYamlUtil.load(fileName, type);
   }
   void addAssertTask(Runnable runnable);
}

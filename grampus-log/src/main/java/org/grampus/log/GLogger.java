package org.grampus.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GLogger {
    private static final Logger logger = LoggerFactory.getLogger(GLogger.class);

   public static void info(String logMessage, Object...parameters){
       logger.info(logMessage,parameters);
   }

   public static void error(String logMessage, Object...parameters){
        logger.error(logMessage,parameters);
   }

    public static void warn(String logMessage, Object...parameters){
        logger.warn(logMessage,parameters);
    }

    public static void debug(String logMessage, Object...parameters){
        logger.warn(logMessage,parameters);
    }
}

package org.grampus.log;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GLogger {
    static final Logger logger = LogManager.getLogger();

   public static void info(String logMessage, Object...parameters){
       logger.info(logMessage,parameters);
   }

   public static void error(String logMessage, Object...parameters){
        logger.error(logMessage,parameters);
   }

    public static void warn(String logMessage, Object...parameters){
        logger.warn(logMessage,parameters);
    }
}

package org.grampus.core.plugin;

import org.grampus.core.GProcessor;
import org.grampus.log.GLogger;

import java.util.Map;

public class GPluginProcessor<T> extends GProcessor<T> {
    @Override
    public void handle(T payload, Map meta) {
        if(payload instanceof GPluginMessage){
            stubHandle((GPluginMessage)payload,meta);
        }else {
            super.handle(payload, meta);
        }
    }

    public void stubHandle(GPluginMessage pluginMessage, Map meta) {
        try {
            pluginMessage.getMethod().invoke(this, pluginMessage.getArgs());
        } catch (Exception e) {
            GLogger.error("failure to handle plugin API [{}], with {}", pluginMessage, e);
        }
    }
}

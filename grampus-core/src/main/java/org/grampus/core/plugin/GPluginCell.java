package org.grampus.core.plugin;

import org.grampus.core.GCell;
import org.grampus.log.GLogger;

import java.util.Map;

public class GPluginCell<T> extends GCell<T> {
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

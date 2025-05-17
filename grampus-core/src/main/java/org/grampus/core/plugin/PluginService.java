package org.grampus.core.plugin;

import org.grampus.core.GService;
import org.grampus.core.annotation.plugin.GPlugin;
import org.grampus.log.GLogger;
import org.reflections.Reflections;

import java.util.Set;

public class PluginService extends GService {
    @Override
    protected void init() {
        parsePluginAnnotations();
    }

    private void parsePluginAnnotations() {
        Reflections reflections = new Reflections("org");
        Set<Class<?>> pluginCellClazzs = reflections.getTypesAnnotatedWith(GPlugin.class);
        if(pluginCellClazzs != null && pluginCellClazzs.size() > 0){
            pluginCellClazzs.forEach(pluginClazz->{
                GPlugin gPlugin = pluginClazz.getAnnotation(GPlugin.class);
                try {
                    if(GPluginProcessor.class.isAssignableFrom(pluginClazz)){
                        GPluginProcessor gPluginCell = (GPluginProcessor) pluginClazz.getConstructor().newInstance();
                        process(gPlugin.event(),gPluginCell);
                    }else {
                        GLogger.error("GRestPlugin only can be used for GPluginProcessor, pls double check [{}]",pluginClazz);
                    }
                } catch (Exception e){
                    GLogger.error("failure to init plugin with event [{}]", gPlugin.event());
                }
            });
        }
    }
}


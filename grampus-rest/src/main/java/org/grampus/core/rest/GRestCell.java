package org.grampus.core.rest;
import org.grampus.core.GConstant;
import org.grampus.core.annotation.plugin.GPlugin;
import org.grampus.core.annotation.rest.GRestController;
import org.grampus.core.monitor.GMonitorMap;
import org.grampus.core.plugin.GPluginCell;
import org.grampus.log.GLogger;

import java.io.File;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@GPlugin(event = GConstant.REST_PLUGIN)
public class GRestCell extends GPluginCell<GRestController> {
    public static final String GREST_CONFIG_KEY = "restConfig";
    public static final String REST_DEFAULT_CONFIG_YAML = "plugin/rest.yaml";
    private GRestClient client;
    private AtomicInteger expectedCellCount = new AtomicInteger();
    private AtomicInteger registeredCount = new AtomicInteger();

    @Override
    public void start() {
        onStatus("START", false);
        GRestOptions config = getConfig(GRestOptions.class);
        if (config == null) {
            File file = new File(this.getClass().getClassLoader().getResource(REST_DEFAULT_CONFIG_YAML).getFile());
            if (file.exists()) {
                config = getController().loadConfig(REST_DEFAULT_CONFIG_YAML, GRestOptions.class);
            }
        }
        client = new GRestClient();
        onStatus("START", true);
        startClientAfterReady(config);
    }

    private void startClientAfterReady(GRestOptions config) {
        long startTime = now();
        while (true) {
            if (registeredCount.get() == expectedCellCount.get() ||now()-startTime > config.getInitMaxWaitTimeMills()) {
                client.start(config);
                break;
            }else {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    GLogger.error("GRest start config wait interrupted, {}",e);
                }
            }
        }
    }

    @Override
    public void handle(GRestController restController, Map meta) {
        registeredCount.addAndGet(1);
        if (restController.isValidController()) {
            this.client.registerGRestController(restController);
        }
    }

    @Override
    public void onMonitorListener(String cellId, GMonitorMap monitorMap) {
        expectedCellCount.addAndGet(1);
    }
    @Override
    public String getConfigKey() {
        return GREST_CONFIG_KEY;
    }
}

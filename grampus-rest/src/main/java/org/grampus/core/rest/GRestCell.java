package org.grampus.core.rest;
import org.grampus.core.GConstant;
import org.grampus.core.annotation.plugin.GPlugin;
import org.grampus.core.annotation.rest.GRestController;
import org.grampus.core.plugin.GPluginCell;
import org.grampus.core.rest.GRestOptions;

import java.io.File;
import java.util.Map;

@GPlugin(event = GConstant.REST_PLUGIN)
public class GRestCell extends GPluginCell<GRestController> {
    public static final String REST_DEFAULT_CONFIG_YAML = "plugin/rest.yaml";
    private GRestClient client;
    private Integer registeredCount = 0;

    @Override
    public void start() {
        onStatus("START", false);
        GRestOptions config = getController().getConfig(GRestOptions.GREST_CONFIG_KEY, GRestOptions.class);
        if (config == null) {
            File file = new File(this.getClass().getClassLoader().getResource(REST_DEFAULT_CONFIG_YAML).getFile());
            if (file.exists()) {
                config = getController().loadConfig(REST_DEFAULT_CONFIG_YAML, GRestOptions.class);
            }
        }
        client = new GRestClient();
        onStatus("START", true);
        while (true) {
            if (isAllCellInitRest()) {
                client.start(config);
                break;
            }
        }
    }

    private boolean isAllCellInitRest() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    @Override
    public void handle(GRestController restController, Map meta) {
        registeredCount++;
        if (restController.isValidController()) {
            this.client.registerGRestController(restController);
        }
    }

}

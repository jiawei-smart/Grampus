package org.grampus.micrometer.prometheus;

import org.grampus.core.GConstant;
import org.grampus.core.annotation.plugin.GPlugin;
import org.grampus.core.plugin.GPluginCell;
import org.grampus.log.GLogger;

@GPlugin(event = GConstant.MICROMETER_PROMETHEUS_PLUGIN)
public class GMicrometerPrometheusCell<T> extends GPluginCell<T> {
    private static final String MICROMETER_CONFIG = "micrometerConfig";
    private static final String MICROMETER_CONFIG_FILE = "micrometerConfig.yaml";
    private GMicrometerPrometheusClient client;
    @Override
    public String getConfigKey() {
        return MICROMETER_CONFIG;
    }

    @Override
    public String getConfigFileKey() {
        return MICROMETER_CONFIG_FILE;
    }

    @Override
    public void beforeStart() {
        GMicrometerPrometheusOptions config = this.getConfig(GMicrometerPrometheusOptions.class);
        if(config != null){
            this.client = new GMicrometerPrometheusClient();
            this.client.start(config);
        }else {
            GLogger.info("GMicrometerOptions is null, will ignore GMicrometer cell start");
        }
    }
}

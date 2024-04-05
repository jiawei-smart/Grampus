package org.grampus.fix;

import org.grampus.core.client.GClientConfig;

public class GFixOptions implements GClientConfig {
    public static final String CONFIG_KEY = "fixConfig";
    public static final String DEFAULT_CONFIG_YAML = "fixConfig.yaml";

    private String quickFixConfigFile;
    @Override
    public String getConfigKey() {
        return CONFIG_KEY;
    }

    public String getQuickFixConfigFile() {
        return quickFixConfigFile;
    }

    public void setQuickFixConfigFile(String quickFixConfigFile) {
        this.quickFixConfigFile = quickFixConfigFile;
    }
}

package org.grampus.fix;

import org.grampus.core.client.GAPIConfig;

public class GFixOptions implements GAPIConfig {

    private String quickFixConfigFile;

    public String getQuickFixConfigFile() {
        return quickFixConfigFile;
    }

    public void setQuickFixConfigFile(String quickFixConfigFile) {
        this.quickFixConfigFile = quickFixConfigFile;
    }
}

package org.grampus.replay;

import org.grampus.core.client.GAPIConfig;

public class GReplayOptions implements GAPIConfig {
    private String name;
    private String dataFolder;
    private Double keyByteSize;
    private Double valueByteSize;

    private Long size;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getKeyByteSize() {
        return keyByteSize;
    }

    public void setKeyByteSize(Double keyByteSize) {
        this.keyByteSize = keyByteSize;
    }

    public Double getValueByteSize() {
        return valueByteSize;
    }

    public void setValueByteSize(Double valueByteSize) {
        this.valueByteSize = valueByteSize;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getDataFolder() {
        return dataFolder;
    }

    public void setDataFolder(String dataFolder) {
        this.dataFolder = dataFolder;
    }
}

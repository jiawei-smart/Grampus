package org.grampus.core.client;

public interface GAPIBase<T extends GAPIConfig> {

    boolean start(T config);

    boolean stop();
}

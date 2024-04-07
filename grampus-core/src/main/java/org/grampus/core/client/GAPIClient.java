package org.grampus.core.client;

public interface GAPIClient<T extends GAPIConfig> {

    boolean start(T config);

    boolean stop();
}

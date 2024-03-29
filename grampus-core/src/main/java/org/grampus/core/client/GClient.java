package org.grampus.core.client;

public interface GClient <T extends GClientConfig>{

    boolean start(T config);

    boolean stop();
}

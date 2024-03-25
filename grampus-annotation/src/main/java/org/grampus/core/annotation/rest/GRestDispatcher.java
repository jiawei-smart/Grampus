package org.grampus.core.annotation.rest;

import org.grampus.core.annotation.rest.GRestResp;

public interface GRestDispatcher {
    GRestResp dispatch(String methodPath, Object[] params);
}

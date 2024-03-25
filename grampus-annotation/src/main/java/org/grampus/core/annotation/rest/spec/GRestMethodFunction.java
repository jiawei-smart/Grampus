package org.grampus.core.annotation.rest.spec;

import org.grampus.core.annotation.rest.GRestResp;

public interface GRestMethodFunction{
    GRestResp apply(Object[] params);
}

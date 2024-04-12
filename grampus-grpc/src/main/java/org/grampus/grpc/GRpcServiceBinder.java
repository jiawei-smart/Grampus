package org.grampus.grpc;

import io.grpc.BindableService;

import java.util.List;

public interface GRpcServiceBinder {
    List<BindableService> bind();
}

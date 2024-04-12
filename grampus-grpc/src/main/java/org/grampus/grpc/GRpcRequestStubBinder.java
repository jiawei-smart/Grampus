package org.grampus.grpc;

import io.grpc.Channel;

public interface GRpcRequestStubBinder<T> {
   T newBlockingStub(Channel channel);
   T newFutureStub(Channel channel);
   T newStub(Channel channel);
}

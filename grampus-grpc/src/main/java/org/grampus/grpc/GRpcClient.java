package org.grampus.grpc;

import io.grpc.*;
import org.grampus.core.client.GAPIBase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class GRpcClient implements GAPIBase<GRpcOptions> {
    private Map<Integer, ManagedChannel> channelPool;
    private GRpcServiceBinder serviceBinder;

    private Random channelPoolSelector = new Random();
    @Override
    public boolean start(GRpcOptions config) {
        boolean isStarted = false;
        if(config.isConfigClient()){
            initChannelPool(config);
            isStarted = true;
        }
        if(config.isConfigServer()){
            ServerBuilder serverBuilder = ServerBuilder.forPort(config.getListenPort());
            List<BindableService> bindableServices =  serviceBinder.bind();
            bindableServices.forEach(service->serverBuilder.addService(service));
            try {
                Server server =  serverBuilder.build();
                server.start();
//                server.awaitTermination();
                isStarted = true;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return isStarted;
    }

    private void initChannelPool(GRpcOptions config) {
        channelPool = new HashMap<>();
        for(int i = 0;i<config.getChannelPoolSize();i++){
            channelPool.put(i, ManagedChannelBuilder.forAddress(config.getClientHost(), config.getClientPort())
                    .usePlaintext().build());
        }
    }

    @Override
    public boolean stop() {
        return false;
    }

//    public <T> T blockingRequest(GRpcRequestStubBinder stub, T type) throws Exception {
//        Method newBlockingStubMethod = stub.getClass().getMethod("newBlockingStub", Channel.class);
//        newBlockingStubMethod.setAccessible(true);
//        return (T) newBlockingStubMethod.invoke(stub,channelPool.get(channelPoolSelector.nextInt(channelPool.size())));
//    }
//    public <T> T newRequest(GRpcRequestStubBinder stub, T type) throws Exception {
//        Method newBlockingStubMethod = stub.getClass().getMethod("newStub", Channel.class);
//        newBlockingStubMethod.setAccessible(true);
//        return (T) newBlockingStubMethod.invoke(stub,channelPool.get(channelPoolSelector.nextInt(channelPool.size())));
//    }
//
//    public <T> T newFutureRequest(GRpcRequestStubBinder stub, T type) throws Exception {
//        Method newBlockingStubMethod = stub.getClass().getMethod("newFutureStub", Channel.class);
//        newBlockingStubMethod.setAccessible(true);
//        return (T) newBlockingStubMethod.invoke(stub,channelPool.get(channelPoolSelector.nextInt(channelPool.size())));
//    }

    public void setServiceBinder(GRpcServiceBinder serviceBinder) {
        this.serviceBinder = serviceBinder;
    }

    public Channel requestChannel(){
        return channelPool.get(channelPoolSelector.nextInt(channelPool.size()));
    }
}

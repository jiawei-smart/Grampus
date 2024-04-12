package org.grampus.grpc;

import io.grpc.BindableService;
import io.grpc.Channel;
import org.grampus.core.GCell;
import org.grampus.core.message.GMessageHeader;

import java.util.List;
import java.util.Map;

public class GRpcCell extends GCell{
    public static final String GRPC_CONFIG= "rpcConfig";
    @Override
    public String getConfigKey() {
        return GRPC_CONFIG;
    }

    private GRpcClient client;
    @Override
    public void start() {
        GRpcOptions gRpcOptions = (GRpcOptions) getConfig(GRpcOptions.class);
        if(gRpcOptions != null){
            client = new GRpcClient();
            client.setServiceBinder(getServiceBinder());
            client.start(gRpcOptions);
        }
    }

    public GRpcServiceBinder getServiceBinder() {
        return null;
    }

    public Channel requestChannel(){
       return this.client.requestChannel();
    }


//    @Override
//    public Object handle(GMessageHeader header, Object payload, Map meta) {
////        this.client
//    }
}

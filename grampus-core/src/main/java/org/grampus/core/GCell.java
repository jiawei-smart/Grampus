package org.grampus.core;

import org.grampus.core.message.GMessage;
import org.grampus.log.GLogger;

public class GCell {
    private String event;
    private GContext context;

    void init(){
        try {
            start();
            registerRest();
        }catch (Exception e){
            GLogger.error("failure to init cell for event:[{}] with {}",event,e);
        }
    }

    private void registerRest() {

    }

    public void start(){

    }

    public void onNextEvent(String event, GMessage message){

    }
}

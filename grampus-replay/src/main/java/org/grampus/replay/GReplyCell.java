package org.grampus.replay;

import org.grampus.core.GCell;
import org.grampus.core.annotation.rest.GRestGet;
import org.grampus.core.annotation.rest.GRestParam;
import org.grampus.core.annotation.rest.GRestPost;
import org.grampus.core.annotation.rest.GRestResp;
import org.grampus.core.annotation.rest.spec.GRestGroupSpec;
import org.grampus.core.message.GMessageHeader;
import org.grampus.log.GLogger;

import java.util.Map;
import java.util.Set;

public class GReplyCell<T> extends GCell<T> {
    public static final String REPLAY_CONFIG = "replayConfig";
    public static final String REPLAY_CONFIG_YAML = "replayConfig.yaml";

    private GReplayClient client;

    @Override
    public void start() {
        onStatus("replay_init", false);
        GReplayOptions config = (GReplayOptions) getConfig(GReplayOptions.class);
        if (config != null) {
            client = new GReplayClient();
            client.setMarsheller(getMarsheller());
            onStatus("replay_init", client.start(config));
        } else {
            GLogger.error("GReplay init failure due to cannot load a config");
        }
    }

    private GMarsheller getMarsheller() {
        return new SerializableMarsheller();
    }

    @Override
    public String getConfigKey() {
        return REPLAY_CONFIG;
    }

    @Override
    public String getConfigFileKey() {
        return REPLAY_CONFIG_YAML;
    }

    @Override
    public Object handle(GMessageHeader header, T payload, Map meta) {
        getController().submitTask(() -> {
            try {
                this.client.save(getMessageKey(payload), payload);
            } catch (Exception e) {
                GLogger.error("GReplay failure to save message [{}] , with {}", payload, e);
            }
        });
        return payload;
    }

    public String getMessageKey(T payload) {
        return payload.toString();
    }

    @Override
    public GRestGroupSpec getRestGroupSpec() {
        return new GRestGroupSpec(getEvent());
    }

    @GRestGet(path = "/getKeys")
    public Set<String> getKeys() {
        return this.client.getKeys();
    }

    @GRestPost(path = "/replay")
    public GRestResp replay(@GRestParam(name = "messageKey") String messageKey) {
        try {
            Object value = this.client.get(messageKey);
            if (value != null) {
                onEvent(value);
                return GRestResp.responseResp("GReplay replay message success: " + value);
            } else {
                return GRestResp.errorResp("GReplay cannot found a message for key: " + messageKey);
            }
        } catch (Exception e) {
            return GRestResp.errorResp("GReplay replay message failure " + e);
        }
    }
}

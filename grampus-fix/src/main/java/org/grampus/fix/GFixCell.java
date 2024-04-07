package org.grampus.fix;

import org.grampus.core.GCell;
import org.grampus.log.GLogger;
import org.grampus.util.GFileUtil;
import quickfix.*;

import java.util.Map;

public class GFixCell extends GCell {
    public static final String CONFIG_KEY = "fixConfig";
    public static final String DEFAULT_CONFIG_YAML = "fixConfig.yaml";
    public static final String ON_CREATE = "onCreate";
    public static final String ON_LOGON = "onLogon";
    public static final String ON_LOGOUT = "onLogout";
    public static final String TO_ADMIN = "toAdmin";
    public static final String FROM_ADMIN = "fromAdmin";
    public static final String FROM_APP = "fromApp";
    public static final String TO_APP = "toApp";

    public static final String SESSION_ID = "sessionID";

    private GFixClient client;

    @Override
    public void start() {
        onStatus("Quickfix init", false);
        GFixOptions gFixOptions = (GFixOptions) getConfig(GFixOptions.class);
        if (gFixOptions == null && GFileUtil.isExistedInClasspath(DEFAULT_CONFIG_YAML)) {
            gFixOptions = getController().loadConfig(DEFAULT_CONFIG_YAML, GFixOptions.class);
        }
        if (gFixOptions != null && this.client == null) {
            this.client = new GFixClient();
        }
        this.client.setHandler(new GFixMsgHandler() {
            @Override
            public void onCreate(SessionID sessionID) {
                GLogger.info("Quickfix session create, {}", sessionID);
                onEvent(ON_CREATE, sessionID, meta(SESSION_ID, sessionID));
            }

            @Override
            public void onLogon(SessionID sessionID) {
                GLogger.info("Quickfix session logon, {}", sessionID);
                onEvent(ON_LOGON, sessionID, meta(SESSION_ID, sessionID));
            }

            @Override
            public void onLogout(SessionID sessionID) {
                GLogger.info("Quickfix session logout, {}", sessionID);
                onEvent(ON_LOGOUT, sessionID, meta(SESSION_ID, sessionID));
            }

            @Override
            public void toAdmin(Message message, SessionID sessionID) {
                GLogger.info("Quickfix session [{}],toAdmin [{}]", sessionID, message);
                onEvent(TO_ADMIN, message, meta(SESSION_ID, sessionID));
            }

            @Override
            public void fromAdmin(Message message, SessionID sessionID)  {
                GLogger.info("Quickfix session [{}],fromAdmin [{}]", sessionID, message);
                onEvent(FROM_ADMIN, message, meta(SESSION_ID, sessionID));
            }

            @Override
            public void toApp(Message message, SessionID sessionID){
                GLogger.info("Quickfix session [{}],toApp [{}]", sessionID, message);
                onEvent(TO_APP, message, meta(SESSION_ID, sessionID));
            }

            @Override
            public void fromApp(Message message, SessionID sessionID) {
                GLogger.info("Quickfix session [{}],fromApp [{}]", sessionID, message);
                onEvent(FROM_APP, message, meta(SESSION_ID, sessionID));
            }
        });
        onStatus("Quickfix init", this.client.start(gFixOptions));
    }

    @Override
    public void handle(Object payload, Map meta) {
        if (this.client != null) {
            if (payload instanceof String) {
                this.client.sendMessage((String) payload);
            } else if (payload instanceof Message) {
                this.client.sendMessage((Message) payload);
            }
        }
    }

    @Override
    public String getConfigKey() {
        return CONFIG_KEY;
    }
}

package org.grampus.fix;

import org.grampus.core.GCell;
import org.grampus.log.GLogger;
import org.grampus.util.GFileUtil;
import quickfix.*;

import java.util.Map;

public class QFixCell extends GCell {
    public static final String ON_CREATE = "onCreate";
    public static final String ON_LOGON = "onLogon";
    public static final String ON_LOGOUT = "onLogout";
    public static final String TO_ADMIN = "toAdmin";
    public static final String FROM_ADMIN = "fromAdmin";
    public static final String FROM_APP = "fromApp";
    public static final String TO_APP = "toApp";

    public static final String SESSION_ID = "sessionID";

    private QFixClient client;

    @Override
    public void start() {
        onStatus("Quickfix init", false);
        QFixOptions qFixOptions = this.getController().getConfig(QFixOptions.CONFIG_KEY, QFixOptions.class);
        if (qFixOptions == null && GFileUtil.isExistedInClasspath(QFixOptions.DEFAULT_CONFIG_YAML)) {
            qFixOptions = getController().loadConfig(QFixOptions.DEFAULT_CONFIG_YAML, QFixOptions.class);
        }
        if (qFixOptions != null && this.client == null) {
            this.client = new QFixClient();
        }
        this.client.setHandler(new QFixMsgHandler() {
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
        onStatus("Quickfix init", this.client.start(qFixOptions));
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
}

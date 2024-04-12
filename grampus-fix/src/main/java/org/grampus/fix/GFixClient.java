package org.grampus.fix;

import org.grampus.core.client.GAPIBase;
import org.grampus.log.GLogger;
import org.grampus.util.GStringUtil;
import quickfix.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import static quickfix.SessionFactory.ACCEPTOR_CONNECTION_TYPE;
import static quickfix.SessionFactory.SETTING_CONNECTION_TYPE;

public class GFixClient implements GAPIBase<GFixOptions>, Application {
    private GFixMsgHandler handler;
    private SocketAcceptor acceptor;
    private SocketInitiator initiator;

    @Override
    public boolean start(GFixOptions config) {
        if (config == null) {
            GLogger.error("Quickfix client config is null");
            return false;
        } else if (this.handler == null) {
            GLogger.error("Quickfix client handler is null");
            return false;
        }
        try {
            SessionSettings settings = new SessionSettings(config.getQuickFixConfigFile());
            MessageStoreFactory messageStoreFactory = new FileStoreFactory(settings);
            MessageFactory messageFactory = new DefaultMessageFactory();
            LogFactory logFactory = new FileLogFactory(settings);
            if (isAcceptor(settings)) {
                acceptor = new SocketAcceptor(this, messageStoreFactory, settings, logFactory, messageFactory);
                acceptor.start();
            } else {
                initiator = new SocketInitiator(this, messageStoreFactory, settings, logFactory, messageFactory);
                initiator.start();
            }
            return true;
        } catch (ConfigError e) {
            GLogger.error("Quickfix client start failure, with {}", e);
            return false;
        }
    }

    private boolean isAcceptor(SessionSettings settings) throws ConfigError {
        return GStringUtil.equals(settings.getString(SETTING_CONNECTION_TYPE), ACCEPTOR_CONNECTION_TYPE);
    }

    public void sendMessage(Message message) {
        try {
            Session.sendToTarget(message);
        } catch (SessionNotFound e) {
            GLogger.error("QFixClient: cannot found session to send message {}, with [{}]", message, e);
        }
    }

    public void sendMessage(String messageStr) {
        try {
            SessionID sessionID = MessageUtils.getSessionID(messageStr);
            Session session = Session.lookupSession(sessionID);
            if (session != null) {
                Message message = MessageUtils.parse(session, messageStr);
                session.send(message);
            } else {
                GLogger.error("QFixClient: cannot found session to send message {}", messageStr);
            }
        } catch (InvalidMessage e) {
            GLogger.error("QFixClient: failure to parse message str, {} ,{}", messageStr, e);
        }
    }

    @Override
    public boolean stop() {
        if (initiator != null) {
            this.initiator.stop();
        } else if (acceptor != null) {
            this.acceptor.stop();
        }
        return true;
    }

    @Override
    public void onCreate(SessionID sessionID) {
        this.handler.onCreate(sessionID);
    }

    @Override
    public void onLogon(SessionID sessionID) {
        this.handler.onLogon(sessionID);
    }

    @Override
    public void onLogout(SessionID sessionID) {
        this.handler.onLogout(sessionID);
    }

    @Override
    public void toAdmin(Message message, SessionID sessionID) {
        this.handler.toAdmin(message, sessionID);
    }

    @Override
    public void fromAdmin(Message message, SessionID sessionID) throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, RejectLogon {
        this.handler.fromAdmin(message, sessionID);
    }

    @Override
    public void toApp(Message message, SessionID sessionID) throws DoNotSend {
        this.handler.toApp(message, sessionID);
    }

    @Override
    public void fromApp(Message message, SessionID sessionID) throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {
        this.handler.fromApp(message, sessionID);
    }

    public GFixMsgHandler getHandler() {
        return handler;
    }

    public void setHandler(GFixMsgHandler handler) {
        this.handler = handler;
    }

    public List<String> getAllSessions() throws Exception {
        Field field = Session.class.getDeclaredField("sessions");
        field.setAccessible(true);
        ConcurrentMap<SessionID, Session> sessions = (ConcurrentMap<SessionID, Session>) field.get(null);
        return sessions.keySet().stream().map(sessionID -> sessionID.toString()).collect(Collectors.toList());
    }

    public boolean stopSession(String sessionIDStr) throws IOException {
        SessionID sessionID = new SessionID(sessionIDStr);
        Session session = Session.lookupSession(sessionID);
        session.close();
        return true;
    }

    public boolean sendMessage(String sessionIdStr, String message) throws InvalidMessage {
        SessionID sessionID = new SessionID(sessionIdStr);
        Session session = Session.lookupSession(sessionID);
        if(session != null){
            session.send(MessageUtils.parse(session,message));
            return true;
        }else {
            throw new RuntimeException("cannot found the session "+sessionIdStr);
        }
    }
}

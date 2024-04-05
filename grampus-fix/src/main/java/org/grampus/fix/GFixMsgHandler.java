package org.grampus.fix;

import quickfix.*;

public interface GFixMsgHandler {
    void onCreate(SessionID sessionID);
    void onLogon(SessionID sessionID);

    void onLogout(SessionID sessionID);
    void toAdmin(Message message, SessionID sessionID);

    void fromAdmin(Message message, SessionID sessionID) throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, RejectLogon;

    void toApp(Message message, SessionID sessionID) throws DoNotSend;
    void fromApp(Message message, SessionID sessionID) throws  FieldNotFound, IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType;
}

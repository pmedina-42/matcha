package org.example.web.socket;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import org.example.web.utils.JWTUtils;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static org.example.constants.Constants.AUTH_HEADER;

public class ChatSocketHandler extends WebSocketAdapter {
    private static final Set<Session> sessions = ConcurrentHashMap.newKeySet();
    private static final Map<Session, String> userNames = new ConcurrentHashMap<>();

    @Override
    public void onWebSocketConnect(Session session) {
        super.onWebSocketConnect(session);
        String token = session.getUpgradeRequest().getHeader(AUTH_HEADER).substring("Bearer ".length());
        String username = JWTUtils.getUsernameFromToken(token);
        if (username == null) {
            session.close(1008, "Invalid token");
            return;
        }
        sessions.add(session);
        userNames.put(session, username);
    }

    @Override
    public void onWebSocketText(String message) {
        // Process the message in the format: "recipientUsername:messageContent"
        String[] parts = message.split(":", 2);
        if (parts.length < 2) {
            return;
        }
        String recipientUsername = parts[0];
        String messageContent = parts[1];
        Session senderSession = getSession();
        String senderUsername = userNames.get(senderSession);
        userNames.forEach((session, username) -> {
            if (username.equals(recipientUsername) && session.isOpen() && session != senderSession) {
                try {
                    session.getRemote().sendString("Message from " + senderUsername + ": " + messageContent);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onWebSocketClose(int statusCode, String reason) {
        Session closedSession = getSession();
        sessions.remove(closedSession);
        userNames.remove(closedSession);
        super.onWebSocketClose(statusCode, reason);
    }
}
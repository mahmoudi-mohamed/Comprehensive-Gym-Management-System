package com.project.bodify;

import org.springframework.stereotype.Component;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint(value = "/webSocket")
public class Socket {

    private static final Map<Session, Socket> activeSessions = new ConcurrentHashMap<>();
    private Session session;

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        activeSessions.put(session, this);
        System.out.println("WebSocket connection established: Session ID " + session.getId());
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        broadcast(message);
    }

    @OnClose
    public void onClose(Session session) {
        activeSessions.remove(session);
        System.out.println("WebSocket connection closed: Session ID " + session.getId());
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
       // System.err.println("WebSocket error occurred on Session ID " + session.getId() + ": " + throwable.getMessage());
    }

    public static void broadcast(String message) {
        for (Socket socket : activeSessions.values()) {
            try {
                synchronized (socket.session) {
                    socket.session.getBasicRemote().sendText(message);
                }
            } catch (IOException e) {
                System.err.println("Error broadcasting message to Session ID " + socket.session.getId() + ": " + e.getMessage());
            }
        }
    }
}

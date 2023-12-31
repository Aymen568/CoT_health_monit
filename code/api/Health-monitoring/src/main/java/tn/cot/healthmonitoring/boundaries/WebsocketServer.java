package tn.cot.healthmonitoring.boundaries;

import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

@ServerEndpoint("/websocket")
public class WebsocketServer {

    private static final CopyOnWriteArrayList<Session> sessions = new CopyOnWriteArrayList<>();

    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
    }

    @OnError
    public void onError(Session session, Throwable error){
        System.out.println("Push WebSocket error for ID " + session.getId() + ": " + error.getMessage());
    }

    public static void sendToAll(String message) {
        for (Session session : sessions) {
            sendToSession(session, message);
        }
    }

    private static void sendToSession(Session session, String message) {
        RemoteEndpoint.Basic remote = session.getBasicRemote();
        try {
            remote.sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
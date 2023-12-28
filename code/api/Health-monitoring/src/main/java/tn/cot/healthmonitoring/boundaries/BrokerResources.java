package tn.cot.healthmonitoring.boundaries;


import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import tn.cot.healthmonitoring.services.MqttConnection;


import java.util.Hashtable;

@ServerEndpoint("/mqtt")
public class BrokerResources {
    private MqttConnection mqttConnection;
    private static Hashtable<String, Session> sessions = new Hashtable<>();

    @OnOpen
    public void onOpen(Session session){
        mqttConnection.start();
        sessions.put(session.getId(), session); //add the new session

    }
    @OnClose
    public void onClose(Session session, CloseReason reason){
        sessions.remove(session); // delete sessions when client leave
    }
    @OnError
    public void onError(Session session, Throwable error){
        System.out.println("Push WebSocket error for ID " + session.getId() + ": " + error.getMessage());
    }


}
package edu.ics.uci.resources;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;

@ServerEndpoint("/api/ws-test")
public class WebSocketTest {

    public static HashMap<String, Session> websocketSessionMap = new HashMap<>();

    public static BiMap<String, String> userWebSocketMap = HashBiMap.create();


    @OnOpen
    public void myOnOpen(final Session session) throws IOException {
        session.getAsyncRemote().sendText("welcome");
        websocketSessionMap.put(session.getId(), session);
    }

    @OnMessage
    public void myOnMsg(final Session session, String message) {
        if (message.contains("username")) {
            userWebSocketMap.put(message.toLowerCase().substring("username:".length()).trim(), session.getId());
            session.getAsyncRemote().sendText("logged in");
        } else {
            session.getAsyncRemote().sendText(userWebSocketMap.inverse().get(session.getId()) + ", " + message.toUpperCase());
            websocketSessionMap.get(session.getId()).getAsyncRemote().sendText("check check");
        }
    }

    @OnClose
    public void myOnClose(final Session session, CloseReason cr) {
        websocketSessionMap.remove(session.getId());
    }

}

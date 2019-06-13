package edu.ics.uci.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import edu.ics.uci.core.MiddlewareRequest;
import edu.ics.uci.core.TutorNotification;
import edu.ics.uci.core.TutorSubscribeRequest;

import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@ServerEndpoint("/api/push")
public class PushServer {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static HashMap<String, Session> websocketSessionMap = new HashMap<>();
    public static BiMap<String, String> userWebSocketMap = HashBiMap.create();


    public static void sendPushNotifications(MiddlewareRequest middlewareRequest) throws Exception {
        for (String tutorEmail : middlewareRequest.getTutorEmails()) {
            if (userWebSocketMap.containsKey(tutorEmail)) {
                TutorNotification tutorNotification = new TutorNotification(
                        middlewareRequest.getOrigin(), middlewareRequest.getUserEmail(), middlewareRequest.getUserCoordinates()
                );
                String sessionId = userWebSocketMap.get(tutorEmail);
                websocketSessionMap.get(sessionId).getAsyncRemote().sendText(OBJECT_MAPPER.writeValueAsString(tutorNotification));
            }
        }
    }

    @OnOpen
    public void myOnOpen(final Session session) throws IOException {
        websocketSessionMap.put(session.getId(),session);
    }

    @OnMessage
    public void onMsg(final Session session, String message) throws IOException{
        if (message.contains("tutorEmail")){
            try{
                TutorSubscribeRequest request = OBJECT_MAPPER.readValue(message, TutorSubscribeRequest.class);
                String useremail = request.getTutorEmail();
                userWebSocketMap.put(useremail, session.getId());
            }catch(JsonProcessingException e){
                throw e;
            }
        }else{
            /*
            websocketSessionMap.get(session.getId()).getAsyncRemote().sendText(OBJECT_MAPPER.writeValueAsString(new TutorNotification("localhost:8080", "test@uci.edu")));
            if (userWebSocketMap.containsKey("test@uci.edu")){
                String sessionId = userWebSocketMap.get("test@uci.edu");
                websocketSessionMap.get(sessionId).getAsyncRemote().sendText("Hello");
            }
             */
        }
    }
}

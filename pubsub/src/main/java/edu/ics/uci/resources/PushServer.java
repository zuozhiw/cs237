package edu.ics.uci.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
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


    public static void sendPushNotifications(String origin, String userEmail, List<String> tutorEmails){
        for(int i=0; i<tutorEmails.size(); i++){
            String email = tutorEmails.get(i);
            if (userWebSocketMap.containsKey(email)){
                String sessionId = userWebSocketMap.get(email);
                try{
                    websocketSessionMap.get(sessionId).getAsyncRemote().sendText(OBJECT_MAPPER.writeValueAsString(new TutorNotification(origin, userEmail)));
                }catch(JsonProcessingException e){

                }

            }
        }
    }

    @OnOpen
    public void myOnOpen(final Session session) throws IOException {
        session.getAsyncRemote().sendText("welcome");
        websocketSessionMap.put(session.getId(),session);
    }

    @OnMessage
    public void onMsg(final Session session, String message) throws IOException{
        if (message.contains("email")){
            try{
                TutorSubscribeRequest request = OBJECT_MAPPER.readValue(message, TutorSubscribeRequest.class);
                String useremail = request.getEmail();
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

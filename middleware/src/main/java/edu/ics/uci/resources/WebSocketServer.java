package edu.ics.uci.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import edu.ics.uci.core.UserRequestBean;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;

@ServerEndpoint("api/wsserv")
public class WebSocketServer {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static HashMap<String, Session> websocketSessionMap = new HashMap<>();
    public static BiMap<String, String> userWebSocketMap = HashBiMap.create();



    @OnOpen
    public void myOnOpen(final Session session) throws IOException {
        session.getAsyncRemote().sendText("connected");
        websocketSessionMap.put(session.getId(), session);
    }

    @OnMessage
    public void myOnMsg(final Session session, String message) throws IOException{
        try{
            UserRequestBean userRequest = OBJECT_MAPPER.readValue(message, UserRequestBean.class);
            String userEmail = userRequest.getUserEmail();
            userWebSocketMap.put(userEmail, session.getId());
            //TODO: Get current user's location





            //TODO: Select all tutors from tutors database with the required skill


            //TODO: read the TIPPERS database for each tutor selected


            int iterations = 0;
            while (iterations < 3) {

                //TODO: filter tutors by geolocation


                //TODO: POST to Push Server


                //TODO: Check if current request is complete



                iterations++;
            }
        }catch(JsonProcessingException e){
            throw e;
        }
    }

    @OnClose
    public void myOnClose(final Session session, CloseReason cr) {
        websocketSessionMap.remove(session.getId());
    }
}
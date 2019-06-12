package edu.ics.uci.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import edu.ics.uci.core.UserRequestBean;
import org.jdbi.v3.core.Jdbi;

import javax.websocket.*;
import javax.websocket.server.ServerEndpointConfig;
import java.io.IOException;
import java.util.HashMap;

// endpoint path see cs237Application.run
public class ReserveWebSocketServer {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static HashMap<String, Session> websocketSessionMap = new HashMap<>();
    public static BiMap<String, String> userWebSocketMap = HashBiMap.create();


    public static class ReserveWebSocketServerConfigurator extends ServerEndpointConfig.Configurator {

        private Jdbi jdbi;

        public ReserveWebSocketServerConfigurator(Jdbi jdbi) {
            this.jdbi = jdbi;
        }

        @Override
        public <T> T getEndpointInstance(Class<T> endpointClass) throws InstantiationException {
            return (T) new ReserveWebSocketServer(jdbi);
        }
    }

    private Jdbi jdbi;

    public ReserveWebSocketServer(Jdbi jdbi) {
        this.jdbi = jdbi;
    }


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


            

            // TODO: Get current user's location





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
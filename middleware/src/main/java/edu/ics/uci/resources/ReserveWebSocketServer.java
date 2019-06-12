package edu.ics.uci.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import edu.ics.uci.core.ReserveSessionState;
import edu.ics.uci.core.TutorBean;
import edu.ics.uci.core.UserRequestBean;
import edu.ics.uci.db.TutorDAO;
import org.jdbi.v3.core.Jdbi;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.server.ServerEndpointConfig;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;

// endpoint path see cs237Application.run
@ServerEndpoint("/api/reserve-ws")
public class ReserveWebSocketServer {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static HashMap<String, Session> websocketSessionMap = new HashMap<>();
    public static BiMap<String, String> userWebSocketMap = HashBiMap.create();

    public static volatile HashMap<String, ReserveSessionState> reserveSessionMap = new HashMap<>();

    public static Semaphore reserveSessionSemaphore = new Semaphore(1);

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

    /*
    @OnMessage
    public void myOnMsg(final Session session, String message) throws IOException, InterruptedException {

        ObjectNode request = OBJECT_MAPPER.readValue(message, ObjectNode.class);
        String skill = request.get("skill").asText();

        TutorDAO tutorDAO = jdbi.onDemand(TutorDAO.class);
        List<TutorBean> tutorsAll = tutorDAO.findAvailableTutorsWithSkill(skill);

        Random rand = new Random();
        for(int i = 0; i < tutorsAll.size(); i++){
            int index = rand.nextInt(TutorResource.randomPoints.size());
            String point = TutorResource.randomPoints.get(index);
            List<Double> coordinates = Arrays.stream(point.split(",")).map(s -> Double.parseDouble(s)).collect(Collectors.toList());
            tutorsAll.get(i).setCoordinates(coordinates);
        }

        List<TutorBean> tutors = tutorsAll.subList(0, 2);


        session.getAsyncRemote().sendText(OBJECT_MAPPER.writeValueAsString(tutors));


        Thread.sleep(3000);
        ObjectNode increaseTextRes = OBJECT_MAPPER.createObjectNode();
        increaseTextRes.put("notification", "search range increased!");
        session.getAsyncRemote().sendText(OBJECT_MAPPER.writeValueAsString(increaseTextRes));

        List<TutorBean> increased = tutorsAll.subList(2, tutorsAll.size());
        session.getAsyncRemote().sendText(OBJECT_MAPPER.writeValueAsString(increased));

        Thread.sleep(3000);

        ObjectNode foundRes = OBJECT_MAPPER.createObjectNode();
        foundRes.put("found", tutors.get(0).getEmail_id());
        ArrayNode arrayNode = OBJECT_MAPPER.createArrayNode();
        tutors.get(0).getCoordinates().forEach(p -> arrayNode.add(p));
        foundRes.set("coordinates", arrayNode);

        session.getAsyncRemote().sendText(OBJECT_MAPPER.writeValueAsString(foundRes));
    }
*/


    @OnMessage
    public void myOnMsg(final Session session, String message) throws IOException{
        try{
            UserRequestBean userRequest = OBJECT_MAPPER.readValue(message, UserRequestBean.class);
            String userEmail = userRequest.getUserEmail();
            userWebSocketMap.put(userEmail, session.getId());
            reserveSessionMap.put(userEmail, new ReserveSessionState(userEmail, false, null));

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

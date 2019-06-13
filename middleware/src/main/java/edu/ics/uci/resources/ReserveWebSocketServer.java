package edu.ics.uci.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import edu.ics.uci.core.*;
import edu.ics.uci.cs237Configuration;
import edu.ics.uci.db.TutorDAO;
import io.dropwizard.jetty.HttpConnectorFactory;
import io.dropwizard.server.DefaultServerFactory;
import okhttp3.*;
import org.jdbi.v3.core.Jdbi;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.server.ServerEndpointConfig;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;

import static edu.ics.uci.resources.TutorResource.*;
import static org.postgresql.core.Oid.JSON;

// endpoint path see cs237Application.run
@ServerEndpoint("/api/reserve-ws")
public class ReserveWebSocketServer {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static HashMap<String, Session> websocketSessionMap = new HashMap<>();
    public static BiMap<String, String> userWebSocketMap = HashBiMap.create();

    public static volatile HashMap<String, ReserveSessionState> reserveSessionMap = new HashMap<>();

    public static volatile HashMap<String, TippersResponse> tippersResponseMap = new HashMap<>();

    public static Semaphore reserveSessionSemaphore = new Semaphore(1);

    public OkHttpClient okHttpClient;

    public cs237Configuration config;

    public static class ReserveWebSocketServerConfigurator extends ServerEndpointConfig.Configurator {

        private Jdbi jdbi;
        private OkHttpClient okHttpClient;
        private cs237Configuration config;

        public ReserveWebSocketServerConfigurator(
                Jdbi jdbi, OkHttpClient okHttpClient, cs237Configuration config
        ) {
            this.jdbi = jdbi;
            this.okHttpClient = okHttpClient;
            this.config = config;
        }

        @Override
        public <T> T getEndpointInstance(Class<T> endpointClass) throws InstantiationException {
            return (T) new ReserveWebSocketServer(jdbi, okHttpClient, config);
        }
    }

    private Jdbi jdbi;

    public ReserveWebSocketServer(
            Jdbi jdbi, OkHttpClient okHttpClient, cs237Configuration config
    ) {
        this.jdbi = jdbi;
        this.okHttpClient = okHttpClient;
        this.config = config;
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
    public void myOnMsg(final Session session, String message) throws Exception{
        try{
            UserRequestBean userRequest = OBJECT_MAPPER.readValue(message, UserRequestBean.class);
            String userEmail = userRequest.getUserEmail();
            userWebSocketMap.put(userEmail, session.getId());
            reserveSessionMap.put(userEmail, new ReserveSessionState(userEmail, false, null));

            // TODO: Get current user's location
            TippersResponse tippersResponse = getTippersResponse(userEmail);;

            String userBuilding = tippersResponse.getPayload().getBuilding();
            List<Double> userCoordinates = BuildingLocations.getBuildingLocation(userBuilding);
            //TODO: Select all tutors from tutors database with the required skill
            TutorDAO tutorDAO = jdbi.onDemand(TutorDAO.class);
            List<TutorBean> tutorBeans = tutorDAO.findAvailableTutorsWithSkill(userRequest.getSkill());
            //TODO: read the TIPPERS database for each tutor selected
            for (int i=0; i<tutorBeans.size(); i++){
                String tutorEmail = tutorBeans.get(i).getEmail_id();
                TippersResponse tempTippersResponse = getTippersResponse(tutorEmail);
                tippersResponseMap.put(tutorEmail, tempTippersResponse);
                tutorBeans.get(i).setCoordinates(BuildingLocations.getBuildingLocation(tempTippersResponse.getPayload().getBuilding()));
            }

            int iterations = 0;
            while (iterations < 3) {
                double minimumDistance = 10;
                switch(iterations){
                    case 0: minimumDistance = 0.1;
                            break;
                    case 1: minimumDistance = 1;
                            break;
                    case 2: minimumDistance = 10;
                            break;
                }


                List<TutorBean> qualifiedTutors = new ArrayList<>();
                //TODO: filter tutors by geolocation and score
                for (int i=0; i<tutorBeans.size(); i++){
                    TutorBean temp = tutorBeans.get(i);
                    if (temp.getCurrentScore()<=50.0){
                        List<Double> coordinates = temp.getCoordinates();
                        double distanceInKilometer = DistanceCalculator.distance(coordinates.get(1),
                                coordinates.get(0),
                                userCoordinates.get(1),
                                userCoordinates.get(0),
                                "K");
                        if(distanceInKilometer < minimumDistance){
                            qualifiedTutors.add(temp);
                        }
                    }
                }

                //TODO: Send List of Tutors to User
                session.getAsyncRemote().sendText(OBJECT_MAPPER.writeValueAsString(qualifiedTutors));

                //TODO: POST to Push Server
                notifyPushServer(new MiddlewareRequest(
                        getSelfUrl(), userEmail, userCoordinates,
                        qualifiedTutors.stream().map(t -> t.getEmail_id()).collect(Collectors.toList())
                ));

                //TODO: Timeout
                Thread.sleep(10000);

                //TODO: Check if current request is complete


                //TODO: If complete, break. Otherwise continue
                iterations++;
            }
        }catch(JsonProcessingException e){
            throw e;
        }catch(InterruptedException e){
        }
    }


    @OnClose
    public void myOnClose(final Session session, CloseReason cr) {
        websocketSessionMap.remove(session.getId());
    }


    private String getSelfUrl() {
        String host = "http://localhost";
        int port = ((HttpConnectorFactory) ((DefaultServerFactory) config.getServerFactory()).getApplicationConnectors().get(0)).getPort();
        return host + ":" + port;
    }



    public void notifyPushServer(MiddlewareRequest middlewareRequest) throws Exception {
        Request request = new Request.Builder()
                .url(PUSH_SERVER + "/api/broadcast")
                .post(RequestBody.create(MediaType.get("application/json; charset=utf-8"),
                        OBJECT_MAPPER.writeValueAsString(middlewareRequest)))
                .build();
        Response response = okHttpClient.newCall(request).execute();
        if (! response.isSuccessful()) {
            throw new Exception("push server down, " + response.body());
        }
        return;
    }


    public TippersResponse getTippersResponse(String subject_id) throws IOException{
        Request request = new Request.Builder()
                .url(MOCK_TIPPERS + "/semanticobservation/getLast?subject_id=" + subject_id + "&limit=1&region=true")
                .build();
        Response response = okHttpClient.newCall(request).execute();
        String responseJsonStr = response.body().string();
        if (responseJsonStr.trim().equalsIgnoreCase("subject not found")) {
            //throw new RuntimeException(subject_id + " is not a tippers user");
            return null;
        }

        List<TippersResponse> tippersResponses = new ObjectMapper().readValue(responseJsonStr, new TypeReference<List<TippersResponse>>() {
        });
        TippersResponse tippersResponse = tippersResponses.get(0);

        return tippersResponse;
    }
}

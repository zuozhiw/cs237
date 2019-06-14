package edu.ics.uci.resources;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import edu.ics.uci.core.BuildingLocations;
import edu.ics.uci.core.ReserveSessionState;
import edu.ics.uci.core.TippersResponse;
import edu.ics.uci.core.TutorBean;
import edu.ics.uci.db.TutorDAO;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jdbi.v3.core.Jdbi;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static edu.ics.uci.resources.ReserveWebSocketServer.reserveSessionSemaphore;
import static edu.ics.uci.resources.ReserveWebSocketServer.tippersResponseMap;

@Path("/tutor")
@Produces(MediaType.APPLICATION_JSON)
public class TutorResource {

    public static final String MOCK_TIPPERS = "http://localhost:7080";
    public static final String PUSH_SERVER = "http://localhost:9080";

    public static final String SENSORAHOST = "http://sensoria.ics.uci.edu:8059";
    public static final String SENSORAUCIHOST = "http://sensoria.ics.uci.edu:9001";

    public static final String MUTUAL_EXCLUSION_MODULE_HOST = "http://localhost:8180";

    private Jdbi jdbi;
    private OkHttpClient okHttpClient;

    public static List<String> randomPoints = Arrays.asList("-117.841989,33.643161",
            "-117.841968,33.643054", "-117.841721,33.643242", "-117.843356,33.643414",
            "-117.843524,33.643407", "-117.843588,33.64342", "-117.828206,33.643422",
            "-117.827527,33.643288", "-117.827672,33.643708", "-117.841897,33.644132",
            "-117.84173,33.644244", "-117.841532,33.644275", "-117.841773,33.644481",
            "-117.84465,33.646292", "-117.844961,33.646131", "-117.844725,33.646131");

    public TutorResource(Jdbi jdbi, OkHttpClient okHttpClient) {
        this.jdbi = jdbi;
        this.okHttpClient = okHttpClient;
    }

    @GET
    @Path("/list")
    public List<TutorBean> listAllTutors() {
        TutorDAO tutorDAO = jdbi.onDemand(TutorDAO.class);
        return tutorDAO.listAllTutors();
    }

    @GET
    @Path("/getTutor")
    public TutorBean getTutor(@QueryParam("email_id") Optional<String> email_id) {
        TutorDAO tutorDAO = jdbi.onDemand(TutorDAO.class);
        return tutorDAO.findTutor(email_id.get());
    }

    @GET
    @Path("/listAllSkills")
    public List<String> listAllSkills() {
        return jdbi.withHandle(handle ->
                handle.createQuery("select value from config where keyword = 'skill'")
                        .mapTo(String.class)
                        .list());
    }

    @GET
    @Path("/find")
    public List<TutorBean> findAvailableTutorsWithSkill(@QueryParam("skill") Optional<String> skill) {
        TutorDAO tutorDAO = jdbi.onDemand(TutorDAO.class);
        if (skill.isPresent()) {
            List<TutorBean> tutors = tutorDAO.findAvailableTutorsWithSkill(skill.get());
            Random rand = new Random();
            for (int i = 0; i < tutors.size(); i++) {
                int index = rand.nextInt(randomPoints.size());
                String point = randomPoints.get(index);
                List<Double> coordinates = Arrays.stream(point.split(",")).map(s -> Double.parseDouble(s)).collect(Collectors.toList());
                tutors.get(i).setCoordinates(coordinates);
            }
            return tutors;
        } else {
            return tutorDAO.listAllTutors();
        }
    }

    @DELETE
    @Path("/deleteTutor")
    public void deleteTutor(@FormParam("email_id") Optional<String> email_id) {
        TutorDAO tutorDAO = jdbi.onDemand(TutorDAO.class);
        tutorDAO.deleteTutor(email_id.get());
    }

    @POST
    @Path("/insertTutor")
    public void insertTutor(@FormParam("email_id") Optional<String> email_id, @FormParam("skills") Optional<String> skills, @FormParam("available") Optional<Boolean> available) {
        TutorDAO tutorDAO = jdbi.onDemand(TutorDAO.class);
        tutorDAO.insertTutor(email_id.get(), skills.get(), available.get());
    }

    @POST
    @Path("/updateTutor")
    public void updateTutor(@FormParam("email_id") Optional<String> email_id, @FormParam("skills") Optional<String> skills, @FormParam("available") Optional<Boolean> available) {
        TutorDAO tutorDAO = jdbi.onDemand(TutorDAO.class);
        tutorDAO.updateTutor(email_id.get(), skills.get(), available.get());
    }


    @GET
    @Path("/reserve")
    public Optional<TutorBean> reserveTutor(@QueryParam("skill") Optional<String> skill) {
        // find candidate tutors

        // get the latest location of user and tutor

        // calculate score for each tutor, based on distance, last reserved time, previous priority

        // acquire lock on tutor database, update "available" to false and change "priority" score

        // attempt multiple times, from highest rank to lowest rank, because someone else might write to DB

        // release the lock

        // return the chosen candidate

        //implement pub sub to notify tutor when he is reserved
        throw new UnsupportedOperationException("not implemented");
    }

    @GET
    @Path("/tippers")
    public String getTippersResponse(@QueryParam("subject_id") Optional<String> subject_id) throws Exception {
        try {
            if (!subject_id.isPresent()) {
                throw new RuntimeException("subject id cannot be empty");
            }
            Request request = new Request.Builder()
                    .url(SENSORAHOST + "/semanticobservation/getLast?subject_id=" + subject_id.get() + "&limit=1&region=true")
                    .build();
            Response response = okHttpClient.newCall(request).execute();
            String responseJsonStr = response.body().string();
            if (responseJsonStr.trim().equalsIgnoreCase("subject not found")) {
                throw new RuntimeException(subject_id + " is not a tippers user");
            }

            List<TippersResponse> tippersResponses = new ObjectMapper().readValue(responseJsonStr, new TypeReference<List<TippersResponse>>() {
            });
            TippersResponse tippersResponse = tippersResponses.get(0);

            return tippersResponse.toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @GET
    @Path("/terminate")
    public String terminateTutorReservation(@QueryParam("userEmail") Optional<String> userEmail) {
        if (userEmail.isPresent() && ReserveWebSocketServer.reserveSessionMap.get(userEmail.get()).getSelectedTutorEmail() != null) {
            Request request = new Request.Builder()
                    .url(MUTUAL_EXCLUSION_MODULE_HOST + "/api/token/acquire")
                    .build();
            try {
                Response response = okHttpClient.newCall(request).execute();
                String responseString = response.body().string();
                System.out.println(responseString);
                if (responseString.equals("Lock acquired")) {
                    TutorDAO tutorDAO = jdbi.onDemand(TutorDAO.class);
                    TutorBean tutorBean = tutorDAO.findTutor(ReserveWebSocketServer.reserveSessionMap.get(userEmail.get()).getSelectedTutorEmail());

                    double nextTutorScore = tutorBean.getScore();
                    tutorDAO.updateTutorAvailability(ReserveWebSocketServer.reserveSessionMap.get(userEmail.get()).getSelectedTutorEmail(),
                            nextTutorScore, true, tutorBean.getReserved());
                    Request request2 = new Request.Builder()
                            .url(MUTUAL_EXCLUSION_MODULE_HOST + "/api/token/release")
                            .build();
                    try {
                        Response response2 = okHttpClient.newCall(request2).execute();
                        System.out.println(response2.body().string());
                    } catch (IOException e) {

                    }
                    ReserveWebSocketServer.reserveSessionMap.remove(userEmail.get());
                    //TEST ONLY
                    ObjectMapper objectMapper = new ObjectMapper();
                    ObjectNode notification = objectMapper.createObjectNode();
                    notification.put("notification", "Your tutoring session has ended.");

                    ReserveWebSocketServer.websocketSessionMap.get(ReserveWebSocketServer.userWebSocketMap.get(userEmail.get())).getAsyncRemote().sendText(objectMapper.writeValueAsString(notification));
                    return "Success";
                } else {
                    return "Failed";
                }
            } catch (IOException e) {
                //Failed to acquire lock on tutorsDB
                return "Failed";
            }
        } else {
            return "Invalid Parameters";
        }
    }

    @GET
    @Path("/accept")
    public String acceptTutorReservation(@QueryParam("tutorEmail") Optional<String> tutorEmail,
                                         @QueryParam("userEmail") Optional<String> userEmail) throws Exception {

        if (!tutorEmail.isPresent() || !userEmail.isPresent() || !ReserveWebSocketServer.reserveSessionMap.containsKey(userEmail.get())) {
            //throw new Exception("wrong parameters");
            return "session does not exist";
        }

        boolean success = reserveSessionSemaphore.tryAcquire(1000, TimeUnit.MILLISECONDS);
        if (!success) {
            throw new Exception("cannot acquire lock");
        }

        ReserveSessionState reserveSessionState = ReserveWebSocketServer.reserveSessionMap.get(userEmail.get());
        if (reserveSessionState.isCompleted()) {
            //reservation is already done by another tutor
            reserveSessionSemaphore.release();
            return "Another tutor has been assigned to the user";
        }


        Request request = new Request.Builder()
                .url(MUTUAL_EXCLUSION_MODULE_HOST + "/api/token/acquire")
                .build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            String responseString = response.body().string();
            System.out.println(responseString);
            if (!responseString.equals("Lock acquired")) {
                //Failed to acquire lock on tutorsDB
                reserveSessionSemaphore.release();
                return "Failed";
            }

            TutorDAO tutorDAO = jdbi.onDemand(TutorDAO.class);

            TutorBean tutorBean = tutorDAO.findTutor(tutorEmail.get());
            List<Double> coordinates = BuildingLocations.getBuildingLocation(
                    tippersResponseMap.get(tutorEmail.get()).getPayload().getBuilding()
            );
            tutorBean.setCoordinates(coordinates);

            if (tutorBean.getAvailable()) {
                //TODO: Obtain previous tutor score, calculate next tutor score
                double nextTutorScore = tutorBean.getCurrentScore();
                /*
                if (tutorBean.getReserved()==null){
                    nextTutorScore = 20.0;
                }else{
                    LocalDateTime currentLocalDateTime = LocalDateTime.now();
                    LocalDateTime tempDateTime = LocalDateTime.from(tutorBean.getReserved());
                    long hours = tempDateTime.until(currentLocalDateTime, ChronoUnit.HOURS);
                    nextTutorScore = tutorBean.getScore()-(hours*10.0)+20.0;
                    if (nextTutorScore < 20.0) nextTutorScore = 20.0;
                }*/

                tutorDAO.updateTutorAvailability(tutorEmail.get(), nextTutorScore, false, LocalDateTime.now());
            } else {

            }

            Request request2 = new Request.Builder()
                    .url(MUTUAL_EXCLUSION_MODULE_HOST + "/api/token/release")
                    .build();

            Response response2 = okHttpClient.newCall(request2).execute();
            System.out.println(response2.body().string());


            if (tutorBean.getAvailable()) {
                ReserveWebSocketServer.reserveSessionMap.get(userEmail.get()).setCompleted();
                ReserveWebSocketServer.reserveSessionMap.get(userEmail.get()).setSelectedTutorEmail(tutorEmail.get());
                //TEST ONLY

                ObjectMapper objectMapper = new ObjectMapper();
                ObjectNode foundRes = objectMapper.createObjectNode();
                foundRes.put("found", tutorBean.getEmail_id());
                ArrayNode arrayNode = objectMapper.createArrayNode();
                tutorBean.getCoordinates().forEach(p -> arrayNode.add(p));
                foundRes.set("coordinates", arrayNode);

                ReserveWebSocketServer.websocketSessionMap.get(ReserveWebSocketServer.userWebSocketMap.get(userEmail.get()))
                        .getAsyncRemote().sendText(objectMapper.writeValueAsString(foundRes));

                reserveSessionSemaphore.release();
                return "Success";
            } else {
                reserveSessionSemaphore.release();
                return "Failed";
            }


        } catch (IOException e) {
            //Failed to acquire lock on tutorsDB
            reserveSessionSemaphore.release();
            return "Failed";
        }
    }

}
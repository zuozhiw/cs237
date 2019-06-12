package edu.ics.uci.resources;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ics.uci.core.TippersResponse;
import edu.ics.uci.core.TutorBean;
import edu.ics.uci.db.TutorDAO;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Request;
import okhttp3.Response;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.reflect.ConstructorMapper;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Optional;

@Path("/api/tutor")
@Produces(MediaType.APPLICATION_JSON)
public class TutorResource {

    public static final String SENSORAHOST = "http://sensoria.ics.uci.edu:8059";
    public static final String SENSORAUCIHOST = "http://sensoria.ics.uci.edu:9001";

    private Jdbi jdbi;
    private OkHttpClient okHttpClient;

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
    @Path("/find")
    public List<TutorBean> findAvailableTutorsWithSkill(@QueryParam("skill") Optional<String> skill) {
        TutorDAO tutorDAO = jdbi.onDemand(TutorDAO.class);
        if (skill.isPresent()) {
            return tutorDAO.findAvailableTutorsWithSkill(skill.get());
        } else {
            return tutorDAO.listAllTutors();
        }
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

        throw new UnsupportedOperationException("not implemented");
    }

    @GET
    @Path("/tippers")
    public String getTippersResponse(@QueryParam("subject_id") Optional<String> subject_id) throws Exception {
        try {
            if (! subject_id.isPresent()) {
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



}

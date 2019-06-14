package edu.ics.uci.resources;

import edu.ics.uci.core.MockTippersPayload;
import edu.ics.uci.core.MockTippersResponse;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.text.SimpleDateFormat;
import java.util.*;

@Path("/semanticobservation")
@Produces(MediaType.APPLICATION_JSON)
public class MockTIPPERSResource {

    int counter;
    public Map<String, MockTippersPayload> tippersStore = new TreeMap<>();

    public MockTIPPERSResource(){
        counter = 0;
        tippersStore.put("zuozhiw@uci.edu", new MockTippersPayload("ISG", 2060, 2, "DBH"));
        tippersStore.put("salsudai@uci.edu", new MockTippersPayload("ICS", 180, 1, "ICS"));
        tippersStore.put("baiqiushi@gmail.com", new MockTippersPayload( "Conference", 6011, 6, "DBH"));
        tippersStore.put("avinask1@uci.edu", new MockTippersPayload("Lecture Hall", 1200, 1, "PCB"));
        tippersStore.put("sadeem.alsudais@gmail.com", new MockTippersPayload("SH", 128, 1, "SH"));
        tippersStore.put("yiheng@uci.edu", new MockTippersPayload("Student Affairs", 352, 3, "ICS"));
        tippersStore.put("yuehw@uci.edu", new MockTippersPayload("ISG", 2060, 2, "DBH"));

//        tippersStore.put("mno@uci.edu", new MockTippersPayload("ARC", 100, 1, "ARC"));
    }

    @GET
    @Path("/getLast")
    public List<MockTippersResponse> semanticObservation_getLast(@QueryParam("subject_id") Optional<String> subject_id,
                                                                 @QueryParam("limit") Optional<Integer> limit,
                                                                 @QueryParam("region") Optional<Boolean> region){
        //
        List<MockTippersResponse> ret = new ArrayList<>();

        if (!subject_id.isPresent()||!tippersStore.containsKey(subject_id.get())){
            return ret;
        }

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        MockTippersResponse response = new MockTippersResponse(
          tippersStore.get(subject_id.get()),
          0.50,
          counter,
          1,
                formatter.format(new Date())
        );//.toString();

        ret.add(response);

        counter++;

        return ret;

    }

}

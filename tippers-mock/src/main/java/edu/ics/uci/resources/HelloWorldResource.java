package edu.ics.uci.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.*;

@Path("/hello-world")
@Produces(MediaType.APPLICATION_JSON)
public class HelloWorldResource {

    @GET
    public Map<String, List<Integer>> sayHello(@QueryParam("name") Optional<String> name) {
        //return "hello, " + name.orElse("world");
        Map<String, List<Integer>> ret = new HashMap<>();
        ret.put("Test String", Arrays.asList(2,3,4,5,6,7,123049));
        ret.put("?????", new ArrayList<>());
        return ret;
    }

}

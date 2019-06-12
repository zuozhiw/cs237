package edu.ics.uci.resources;

import edu.ics.uci.core.MiddlewareRequest;

import javax.print.attribute.standard.Media;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Arrays;

@Path("/api")
@Produces(MediaType.APPLICATION_JSON)
public class MiddlewareReceiverResource {

    public MiddlewareReceiverResource(){

    }

    @GET
    @Path("/broadcastTest")
    public String broadcastToTutorsTest(){
        PushServer.sendPushNotifications("localhost:8080", "test@uci.edu", Arrays.asList("123@uci.edu","123@icu.edu"));
        return "Broadcast attempted";
    }

    @POST
    @Path("/broadcast")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String broadcastToTutors(MiddlewareRequest middlewareRequest){
        if (middlewareRequest!=null){
            if (middlewareRequest.getTutorEmails()!=null){
                PushServer.sendPushNotifications(middlewareRequest.getOrigin(),middlewareRequest.getUserEmail(),middlewareRequest.getTutorEmails());
                return "Broadcast attempted";
            }
        }
        return "Broadcast not attempted";
    }


}

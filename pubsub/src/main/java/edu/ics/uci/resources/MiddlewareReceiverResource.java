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

    @POST
    @Path("/broadcast")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String broadcastToTutors(MiddlewareRequest middlewareRequest) throws Exception {
        System.out.println(middlewareRequest);

        if (middlewareRequest!=null){
            if (middlewareRequest.getTutorEmails()!=null){
                PushServer.sendPushNotifications(middlewareRequest);
                return "Broadcast attempted";
            }
        }
        return "Broadcast not attempted";
    }


}

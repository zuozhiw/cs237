package edu.ics.uci.resources;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ics.uci.RA2Token;
import jdk.nashorn.internal.parser.Token;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

@Path("/token")
@Produces(MediaType.APPLICATION_JSON)
public class TokenResource {

    private volatile RA2Token myToken;

    Semaphore TokenSemaphore;

    private volatile int myState;

    private List<String> peers;

    private OkHttpClient okHttpClient;

    public TokenResource(OkHttpClient okHttpClient){
        this.TokenSemaphore = new Semaphore(1);
        this.myToken = null;//new RA2Token();
        peers = new ArrayList<>();
        this.okHttpClient = okHttpClient;
    }

    @GET
    @Path("/createToken")
    public String createToken(@QueryParam("token_id") Optional<String> token_id){
        this.myToken = new RA2Token(Integer.parseInt(token_id.get()));
        return "Token Created!";
    }

    @GET
    @Path("/addPeer")
    public String addPeer(@QueryParam("peer") Optional<String> peer){
        if (peer.isPresent()){
            peers.add(peer.get());
            return "Peer added";
        }else{
            return "No parameter";
        }
    }

    @GET
    @Path("/acquire")
    public String acquireLock(){
        try{
            boolean success = this.TokenSemaphore.tryAcquire(1000, TimeUnit.MILLISECONDS);
            if(!success){
                return "Lock not acquired";
            }
            if(this.myToken == null){
                //TODO: Request Token to other clients

                for (int i=0; i<peers.size(); i++){
                    Request request = new Request.Builder()
                            .url(peers.get(i)+"/api/token/requestToken")
                            .build();
                    try {
                        Response response = okHttpClient.newCall(request).execute();
                        String responseJsonStr = response.body().string();
                        System.out.println(responseJsonStr);
                        RA2Token responseToken = new ObjectMapper().readValue(responseJsonStr, new TypeReference<RA2Token>(){});
                        if (responseToken.getId()!=-1){
                            this.myToken = responseToken;
                            return "Lock acquired";
                        }
                    }catch(IOException e){
                        continue;
                    }

                }

                this.TokenSemaphore.release();
                return "Lock not acquired";
            }else{
                return "Lock acquired";
            }
        }catch(InterruptedException e){
            System.out.println("Interrupted!");
            return "Lock not acquired";
        }
    }

    @GET
    @Path("/release")
    public String releaseLock(){
        this.TokenSemaphore.release();
        return "Lock released";
    }

    @GET
    @Path("/requestToken")
    public RA2Token tokenRequested(){
        try{
            boolean success = this.TokenSemaphore.tryAcquire(1, TimeUnit.MILLISECONDS);
            if (!success){
                return new RA2Token(-1);
            }else{
                if (this.myToken==null){
                    this.TokenSemaphore.release();
                    return new RA2Token(-1);
                }else{
                    RA2Token ret = new RA2Token(this.myToken);
                    this.myToken = null;
                    this.TokenSemaphore.release();
                    return ret;
                }
            }

        }catch(InterruptedException e){
            System.out.println("Interrupted!");
            return new RA2Token(-1);
        }

    }



}

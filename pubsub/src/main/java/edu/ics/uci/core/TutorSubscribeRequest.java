package edu.ics.uci.core;

public class TutorSubscribeRequest {
    public String email;

    public TutorSubscribeRequest() {
    }

    public TutorSubscribeRequest(String email){
        this.email = email;
    }
    public String getEmail(){
        return this.email;
    }
}

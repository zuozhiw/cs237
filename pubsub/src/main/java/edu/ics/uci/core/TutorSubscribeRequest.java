package edu.ics.uci.core;

public class TutorSubscribeRequest {
    public String tutorEmail;

    public TutorSubscribeRequest() {
    }

    public TutorSubscribeRequest(String tutorEmail){
        this.tutorEmail = tutorEmail;
    }
    public String getTutorEmail(){
        return this.tutorEmail;
    }
}

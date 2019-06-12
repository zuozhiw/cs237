package edu.ics.uci.core;

public class TutorNotification {
    private String origin;
    private String userEmail;

    public TutorNotification(){

    }

    public TutorNotification(String origin, String userEmail){
        this.origin = origin;
        this.userEmail = userEmail;
    }

    public String getOrigin(){
        return origin;
    }

    public String getUserEmail(){
        return userEmail;
    }
}

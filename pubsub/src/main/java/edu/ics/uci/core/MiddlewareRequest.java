package edu.ics.uci.core;

import java.util.ArrayList;
import java.util.List;

public class MiddlewareRequest {
    private String origin;
    private String userEmail;
    private ArrayList<String> tutorEmails;

    public MiddlewareRequest(){

    }

    public MiddlewareRequest(String origin, String userEmail, List<String> tutorEmails){
        this.origin = origin;
        this.userEmail = userEmail;
        this.tutorEmails = new ArrayList<>();
        for (int i=0; i<tutorEmails.size();i++){
            this.tutorEmails.add(tutorEmails.get(i));
        }
    }

    public String getOrigin(){
        return origin;
    }

    public void setOrigin(String origin){
        this.origin = origin;
    }

    public String getUserEmail(){
        return userEmail;
    }

    public void setUserEmail(String userEmail){
        this.userEmail = userEmail;
    }

    public ArrayList<String> getTutorEmails(){
        return tutorEmails;
    }

    public void setTutorEmails(ArrayList<String> tutorEmails){
        this.tutorEmails = new ArrayList<>();
        for (int i=0; i<tutorEmails.size();i++){
            this.tutorEmails.add(tutorEmails.get(i));
        }
    }


}

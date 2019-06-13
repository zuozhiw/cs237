package edu.ics.uci.core;

import java.util.ArrayList;
import java.util.List;

public class MiddlewareRequest {
    private String origin;
    private String userEmail;
    private List<Double> userCoordinates;
    private List<String> tutorEmails;

    public MiddlewareRequest(){

    }

    public MiddlewareRequest(String origin, String userEmail, List<Double> userCoordinates, List<String> tutorEmails) {
        this.origin = origin;
        this.userEmail = userEmail;
        this.userCoordinates = userCoordinates;
        this.tutorEmails = tutorEmails;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public List<Double> getUserCoordinates() {
        return userCoordinates;
    }

    public void setUserCoordinates(List<Double> userCoordinates) {
        this.userCoordinates = userCoordinates;
    }

    public List<String> getTutorEmails() {
        return tutorEmails;
    }

    public void setTutorEmails(List<String> tutorEmails) {
        this.tutorEmails = tutorEmails;
    }
}

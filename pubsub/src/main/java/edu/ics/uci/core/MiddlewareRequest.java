package edu.ics.uci.core;

import java.util.ArrayList;
import java.util.List;

public class MiddlewareRequest {
    private String origin;
    private String userEmail;
    private List<Double> userCoordinates;
    private ArrayList<String> tutorEmails;

    public MiddlewareRequest(){

    }

    public MiddlewareRequest(String origin, String userEmail, List<Double> userCoordinates, ArrayList<String> tutorEmails) {
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

    public ArrayList<String> getTutorEmails() {
        return tutorEmails;
    }

    public void setTutorEmails(ArrayList<String> tutorEmails) {
        this.tutorEmails = tutorEmails;
    }


    @Override
    public String toString() {
        return "MiddlewareRequest{" +
                "origin='" + origin + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", userCoordinates=" + userCoordinates +
                ", tutorEmails=" + tutorEmails +
                '}';
    }
}

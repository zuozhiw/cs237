package edu.ics.uci.core;

import java.util.List;

public class TutorNotification {
    private String origin;
    private String userEmail;
    private List<Double> userCoordinates;

    public TutorNotification(){

    }

    public TutorNotification(String origin, String userEmail, List<Double> userCoordinates) {
        this.origin = origin;
        this.userEmail = userEmail;
        this.userCoordinates = userCoordinates;
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
}

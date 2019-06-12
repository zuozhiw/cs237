package edu.ics.uci.core;

public class ReserveSessionState {
    private String userEmail;
    private boolean completed;
    private String selectedTutorEmail;

    public ReserveSessionState(){

    }

    public ReserveSessionState(String userEmail, boolean completed, String selectedTutorEmail){
        this.userEmail = userEmail;
        this.completed = completed;
        this.selectedTutorEmail = selectedTutorEmail;
    }

    public String getUserEmail(){
        return userEmail;
    }

    public void setUserEmail(String userEmail){
        this.userEmail = userEmail;
    }

    public boolean isCompleted(){
        return completed;
    }

    public void setCompleted(){
        this.completed = true;
    }

    public String getSelectedTutorEmail(){
        return selectedTutorEmail;
    }

    public void setSelectedTutorEmail(String selectedTutorEmail){
        this.selectedTutorEmail = selectedTutorEmail;
    }
}

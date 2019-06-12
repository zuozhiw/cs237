package edu.ics.uci.core;

public class UserRequestBean {
    private String skill;
    private String userEmail;

    public UserRequestBean(){

    }

    public UserRequestBean(String skill, String userEmail, Double longitude, Double latitude){
        this.skill = skill;
        this.userEmail = userEmail;
    }

    public String getSkill(){
        return skill;
    }

    public void setSkill(String skill){
        this.skill = skill;
    }

    public String getUserEmail(){
        return userEmail;
    }

    public void setUserEmail(String userEmail){
        this.userEmail = userEmail;
    }

}
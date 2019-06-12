package edu.ics.uci.core;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

public class TutorBean {

    private String email_id;
    private String skills;
    private LocalDateTime reserved;
    private Boolean available;
    private Double score;
    private List<Double> coordinates;

    public TutorBean() {
    }

    public List<Double> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<Double> coordinates) {
        this.coordinates = coordinates;
    }

    public TutorBean(String email_id, String skills, LocalDateTime reserved, Boolean available, Double score, List<Double> coordinate) {
        this.email_id = email_id;
        this.skills = skills;
        this.reserved = reserved;
        this.available = available;
        this.score = score;
        this.coordinates = coordinate;
    }

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public LocalDateTime getReserved() {
        return reserved;
    }

    public void setReserved(LocalDateTime reserved) {
        this.reserved = reserved;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TutorBean tutorBean = (TutorBean) o;
        return Objects.equals(email_id, tutorBean.email_id) &&
                Objects.equals(skills, tutorBean.skills) &&
                Objects.equals(reserved, tutorBean.reserved) &&
                Objects.equals(available, tutorBean.available) &&
                Objects.equals(score, tutorBean.score) &&
                Objects.equals(coordinates, tutorBean.coordinates);
    }

    @Override
    public String toString() {
        return "TutorBean{" +
                "email_id='" + email_id + '\'' +
                ", skills='" + skills + '\'' +
                ", reserved=" + reserved +
                ", available=" + available +
                ", score=" + score +
                ", coordinates=" + coordinates +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(email_id, skills, reserved, available, score, coordinates);
    }

    public double getCurrentScore(){
        if (this.reserved==null){
            return 20.0;
        }
        LocalDateTime currentLocalDateTime = LocalDateTime.now();
        LocalDateTime tempDateTime = LocalDateTime.from(this.reserved);
        long hours = tempDateTime.until(currentLocalDateTime, ChronoUnit.HOURS);
        double currentTutorScore = this.score-(hours*10.0)+20.0;
        if (currentTutorScore < 20.0) currentTutorScore = 20.0;
        return currentTutorScore;
    }
}

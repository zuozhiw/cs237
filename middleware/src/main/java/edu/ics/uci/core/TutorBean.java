package edu.ics.uci.core;

import java.time.LocalDateTime;
import java.util.Objects;

public class TutorBean {

    private String email_id;
    private String skills;
    private LocalDateTime reserved;
    private Boolean available;

    public TutorBean(String email_id, String skills, LocalDateTime reserved, Boolean available) {
        this.email_id = email_id;
        this.skills = skills;
        this.reserved = reserved;
        this.available = available;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TutorBean tutorBean = (TutorBean) o;
        return Objects.equals(email_id, tutorBean.email_id) &&
                Objects.equals(skills, tutorBean.skills) &&
                Objects.equals(reserved, tutorBean.reserved) &&
                Objects.equals(available, tutorBean.available);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email_id, skills, reserved, available);
    }

    @Override
    public String toString() {
        return "TutorBean{" +
                "email_id='" + email_id + '\'' +
                ", skills='" + skills + '\'' +
                ", reserved=" + reserved +
                ", available=" + available +
                '}';
    }
}

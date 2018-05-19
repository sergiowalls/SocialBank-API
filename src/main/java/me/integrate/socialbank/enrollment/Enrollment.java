package me.integrate.socialbank.enrollment;

import java.util.Objects;

public class Enrollment {
    private String userEmail;
    private int eventId;

    public Enrollment() {}

    public Enrollment(String userEmail, int eventId) {
        this.userEmail = userEmail;
        this.eventId = eventId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Enrollment enrollment = (Enrollment) o;
        return  eventId == enrollment.eventId && Objects.equals(userEmail, enrollment.userEmail);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(userEmail, eventId);
    }
}

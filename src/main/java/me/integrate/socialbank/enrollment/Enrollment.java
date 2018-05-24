package me.integrate.socialbank.enrollment;

import java.util.Objects;

public class Enrollment {
    private String userEmail;
    private int eventId;


    public Enrollment(int eventId, String userEmail) {
        this.userEmail = userEmail;
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
}

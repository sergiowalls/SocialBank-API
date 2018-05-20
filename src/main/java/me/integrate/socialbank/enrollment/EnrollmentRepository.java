package me.integrate.socialbank.enrollment;

import java.util.List;

public interface EnrollmentRepository {

    Enrollment saveEnrollment(String email, int id);

    List<String> getEnrollmentsOfEvent(int id);

    List<Integer> getEnrollmentsOfUser(String email);

    void deleteEnrollment(int id, String email);
}

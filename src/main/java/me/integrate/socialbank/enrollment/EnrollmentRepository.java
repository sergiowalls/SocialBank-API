package me.integrate.socialbank.enrollment;

import java.util.List;

public interface EnrollmentRepository {

    Enrollment saveEnrollment(int id, String email);

    List<String> getEnrollmentsOfEvent(int id);

    int getNumberOfUsersEnrolledInEvent(int id);

    List<Integer> getEnrollmentsOfUser(String email);

    void deleteEnrollment(int id, String email);
}

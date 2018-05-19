package me.integrate.socialbank.enrollment;

import java.util.List;

public interface EnrollmentRepository {

    Enrollment saveEnrollment(Enrollment enrollment);

    List<Enrollment> getEnrollmentsOfEvent(int id);

    List<Enrollment> getEnrollmentsOfUser(String email);
}

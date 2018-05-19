package me.integrate.socialbank.enrollment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnrollmentService {

    private EnrollmentRepository enrollmentRepository;

    @Autowired
    public EnrollmentService(EnrollmentRepository enrollmentRepository)  {
        this.enrollmentRepository = enrollmentRepository;
    }

    public Enrollment saveEnrollment(Enrollment enrollment) {
        return enrollmentRepository.saveEnrollment(enrollment);
    }

    public List<String> getEnrollmentsOfEvent(int id) {
        return enrollmentRepository.getEnrollmentsOfEvent(id);
    }

    public List<Integer> getEnrollmentsOfUser(String email) {
        return enrollmentRepository.getEnrollmentsOfUser(email);
    }

    public void deleteEnrollment(int id, String email) { enrollmentRepository.deleteEnrollment(id, email);
    }
}

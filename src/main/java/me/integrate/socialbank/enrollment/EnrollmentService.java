package me.integrate.socialbank.enrollment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}

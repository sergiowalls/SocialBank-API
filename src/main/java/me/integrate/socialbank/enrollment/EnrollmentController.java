package me.integrate.socialbank.enrollment;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EnrollmentController {

    private EnrollmentService enrollmentService;

    @Autowired
    public EnrollmentController(EnrollmentService enrollmentService) { this.enrollmentService = enrollmentService; }

    @PostMapping("/events/{id}/enroll")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Enrollment enrollEvent(@PathVariable int id, Authentication auth) {
        String email = auth.getName();
        return enrollmentService.saveEnrollment(new Enrollment(email, id));
    }
}

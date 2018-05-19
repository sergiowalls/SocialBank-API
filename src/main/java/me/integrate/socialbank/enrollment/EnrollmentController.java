package me.integrate.socialbank.enrollment;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EnrollmentController {

    private EnrollmentService enrollmentService;

    @Autowired
    public EnrollmentController(EnrollmentService enrollmentService) { this.enrollmentService = enrollmentService; }

    @PostMapping("/events/{id}/enroll")
    @ResponseStatus(HttpStatus.CREATED)
    public Enrollment enrollEvent(@PathVariable int id, Authentication auth) {
        String email = auth.getName();
        return enrollmentService.saveEnrollment(new Enrollment(email, id));
    }

    @GetMapping("/events/{id}/enrollments")
    public @ResponseBody
    List<Enrollment> getEnrollmentsOfEvent(@PathVariable int id) {
        return enrollmentService.getEnrollmentsOfEvent(id);
    }

    @GetMapping("/users/{email}/enrollments")
    public @ResponseBody
    List<Enrollment> getEnrollmentsOfUser(@PathVariable String email) {
        return enrollmentService.getEnrollmentsOfUser(email);
    }
}

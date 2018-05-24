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
    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @PostMapping("/events/{id}/enrollments")
    @ResponseStatus(HttpStatus.CREATED)
    public Enrollment enrollEvent(@PathVariable int id, Authentication auth) {
        return enrollmentService.saveEnrollment(id, auth.getName());
    }

    @GetMapping("/events/{id}/enrollments")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody
    List<String> getEnrollmentsOfEvent(@PathVariable int id) {
        return enrollmentService.getEnrollmentsOfEvent(id);
    }

    @GetMapping("/users/{email}/enrollments")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody
    List<Integer> getEnrollmentsOfUser(@PathVariable String email) {
        return enrollmentService.getEnrollmentsOfUser(email);
    }

    @DeleteMapping("/events/{id}/enrollments")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEvent(@PathVariable int id, Authentication auth) {
        String email = auth.getName();
        enrollmentService.deleteEnrollment(id, email);
    }
}

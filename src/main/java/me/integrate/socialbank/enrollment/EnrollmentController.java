package me.integrate.socialbank.enrollment;


import me.integrate.socialbank.enrollment.exceptions.EnrollmentAlreadyExistsException;
import me.integrate.socialbank.event.Event;
import me.integrate.socialbank.event.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
public class EnrollmentController {

    private EnrollmentService enrollmentService;

    private EventService eventService;

    @Autowired
    public EnrollmentController(EnrollmentService enrollmentService, EventService eventService) {
        this.enrollmentService = enrollmentService;
        this.eventService = eventService;
    }

    @PostMapping("/events/{id}/enroll")
    @ResponseStatus(HttpStatus.CREATED)
    public Enrollment enrollEvent(@PathVariable int id, Authentication auth) {
        Event event = eventService.getEventById(id);
        if (event.getIniDate().before(new Date())) throw new EnrollmentAlreadyExistsException();
        String email = auth.getName();
        if (event.getCreatorEmail().equals(email)) throw new EnrollmentAlreadyExistsException();
        return enrollmentService.saveEnrollment(new Enrollment(email, id));
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
}

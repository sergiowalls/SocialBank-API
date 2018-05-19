package me.integrate.socialbank.enrollment;

import me.integrate.socialbank.event.Event;
import me.integrate.socialbank.event.EventService;
import me.integrate.socialbank.event.EventTestUtils;
import me.integrate.socialbank.user.User;
import me.integrate.socialbank.user.UserService;
import me.integrate.socialbank.user.UserTestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EnrollmentServiceTest {


    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private EventService eventService;

    @Autowired
    private UserService userService;

    @Test
    void givenEnrollmentThenIsStoredCorrectly() {
        String email = "a@a.com";
        User user = UserTestUtils.createUser(email);
        userService.saveUser(user);

        int id = eventService.saveEvent(EventTestUtils.createEvent(email)).getId();

        Enrollment enrollment = new Enrollment(email, id);
        assertEquals(enrollment, enrollmentService.saveEnrollment(enrollment));
    }
}

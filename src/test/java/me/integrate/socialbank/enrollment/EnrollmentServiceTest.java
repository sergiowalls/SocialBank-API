package me.integrate.socialbank.enrollment;

import me.integrate.socialbank.event.EventService;
import me.integrate.socialbank.event.EventTestUtils;
import me.integrate.socialbank.user.User;
import me.integrate.socialbank.user.UserService;
import me.integrate.socialbank.user.UserTestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
@ExtendWith(SpringExtension.class)
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
        assertEquals(enrollment, enrollmentService.saveEnrollment(email, id));
    }

    @Test
    void givenEnrollmentsStoredInDatabaseWhenRetrievedByEventReturnEnrollments() {
        String emailCreator = "a@a.a", emailEnrolledOne = "b@b.b", emailEnrolledTwo = "c@c.c";
        userService.saveUser(UserTestUtils.createUser(emailCreator));
        userService.saveUser(UserTestUtils.createUser(emailEnrolledOne));
        userService.saveUser(UserTestUtils.createUser(emailEnrolledTwo));

        int id = eventService.saveEvent(EventTestUtils.createEvent(emailCreator)).getId();
        List<String> le = new ArrayList<>();
        le.add(emailEnrolledOne); le.add(emailEnrolledTwo);

        enrollmentService.saveEnrollment(emailEnrolledOne, id);
        enrollmentService.saveEnrollment(emailEnrolledTwo, id);

        List<String> retList = enrollmentService.getEnrollmentsOfEvent(id);
        assertTrue(le.containsAll(retList));
        assertTrue(retList.containsAll(le));
    }

    @Test
    void givenEnrollmentsStoredInDatabaseWhenRetrievedByUserReturnEnrollments() {
        String emailCreator = "a@a.a", emailEnrolled = "b@b.b";

        userService.saveUser(UserTestUtils.createUser(emailCreator));
        userService.saveUser(UserTestUtils.createUser(emailEnrolled));

        int idOne = eventService.saveEvent(EventTestUtils.createEvent(emailCreator)).getId();
        int idTwo = eventService.saveEvent(EventTestUtils.createEvent(emailCreator)).getId();
        List<Integer> le = new ArrayList<>();
        le.add(idOne); le.add(idTwo);

        enrollmentService.saveEnrollment(emailEnrolled, idOne);
        enrollmentService.saveEnrollment(emailEnrolled, idTwo);

        List<Integer> retList = enrollmentService.getEnrollmentsOfUser(emailEnrolled);
        assertTrue(le.containsAll(retList));
        assertTrue(retList.containsAll(le));
    }

    @Test
    void givenEnrollmentStoredInDatabaseWhenDeletedThenIsNoLongerStored() {
        String creatorEmail = "email@email.tld";
        String enrolledEmail = "e@e.e";
        userService.saveUser(UserTestUtils.createUser(creatorEmail));
        userService.saveUser(UserTestUtils.createUser(enrolledEmail));
        int eventId = eventService.saveEvent(EventTestUtils.createEvent(creatorEmail)).getId();
        enrollmentService.saveEnrollment(enrolledEmail, eventId);
        enrollmentService.deleteEnrollment(eventId, enrolledEmail);
        List<String> emailsEnrolled = enrollmentService.getEnrollmentsOfEvent(eventId);
        assert (!emailsEnrolled.contains(enrolledEmail));
    }
}

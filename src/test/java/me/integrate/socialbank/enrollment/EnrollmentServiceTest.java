package me.integrate.socialbank.enrollment;

import me.integrate.socialbank.enrollment.exceptions.TooLateException;
import me.integrate.socialbank.event.Event;
import me.integrate.socialbank.event.EventService;
import me.integrate.socialbank.event.EventTestUtils;
import me.integrate.socialbank.user.User;
import me.integrate.socialbank.user.UserService;
import me.integrate.socialbank.user.UserTestUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
        String emailCreator = "amador@arribas.alf";
        String emailEnrolled = "z@z.z";
        User userCreator = UserTestUtils.createUser(emailCreator);
        User userEnrolled = UserTestUtils.createUser(emailEnrolled);
        userService.saveUser(userCreator);
        userService.saveUser(userEnrolled);

        int id = eventService.saveEvent(EventTestUtils.createEvent(emailCreator)).getId();

        Enrollment enrollment = new Enrollment(id, emailEnrolled);
        Enrollment enrollment2 = enrollmentService.saveEnrollment(id, emailEnrolled);
        assertEquals(enrollment, enrollment2);
    }

    @Test
    void givenEnrollmentsStoredInDatabaseWhenRetrievedByEventReturnEnrollments() {
        String emailCreator = "amador@arribas.alf", emailEnrolledOne = "z@z.z", emailEnrolledTwo = "casimiro@calpe.cat";
        userService.saveUser(UserTestUtils.createUser(emailCreator));
        userService.saveUser(UserTestUtils.createUser(emailEnrolledOne));
        userService.saveUser(UserTestUtils.createUser(emailEnrolledTwo));

        int id = eventService.saveEvent(EventTestUtils.createEvent(emailCreator)).getId();
        List<String> le = new ArrayList<>();
        le.add(emailEnrolledOne); le.add(emailEnrolledTwo);

        enrollmentService.saveEnrollment(id, emailEnrolledOne);
        enrollmentService.saveEnrollment(id, emailEnrolledTwo);

        List<String> retList = enrollmentService.getEnrollmentsOfEvent(id);
        assertTrue(le.containsAll(retList));
        assertTrue(retList.containsAll(le));
    }

    @Test
    void givenEnrollmentsStoredInDatabaseWhenRetrievedByUserReturnEnrollments() {
        String emailCreator = "amador@arribas.alf", emailEnrolled = "z@z.z";

        userService.saveUser(UserTestUtils.createUser(emailCreator));
        userService.saveUser(UserTestUtils.createUser(emailEnrolled));

        int idOne = eventService.saveEvent(EventTestUtils.createEvent(emailCreator)).getId();
        int idTwo = eventService.saveEvent(EventTestUtils.createEvent(emailCreator)).getId();
        List<Integer> le = new ArrayList<>();
        le.add(idOne); le.add(idTwo);

        enrollmentService.saveEnrollment(idOne, emailEnrolled);
        enrollmentService.saveEnrollment(idTwo, emailEnrolled);

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
        enrollmentService.saveEnrollment(eventId, enrolledEmail);
        enrollmentService.deleteEnrollment(eventId, enrolledEmail);
        List<String> emailsEnrolled = enrollmentService.getEnrollmentsOfEvent(eventId);
        assertTrue(!emailsEnrolled.contains(enrolledEmail));
    }

    @Test
    void whenAddOrDeleteEnrollmentThenEventIsCorrectlyUpdated() {
        String creatorEmail = "email@email.tld";
        String enrolledEmail = "e@e.e";
        userService.saveUser(UserTestUtils.createUser(creatorEmail));
        userService.saveUser(UserTestUtils.createUser(enrolledEmail));
        Event event = eventService.saveEvent(EventTestUtils.createEvent(creatorEmail));
        assert (event.getNumberEnrolled() == 0);
        int eventId = event.getId();

        enrollmentService.saveEnrollment(eventId, enrolledEmail);
        event = eventService.getEventById(eventId);
        assertEquals(1, (int) event.getNumberEnrolled());

        enrollmentService.deleteEnrollment(eventId, enrolledEmail);
        event = eventService.getEventById(eventId);
        assertEquals(0, (int) event.getNumberEnrolled());
    }

    @Test
    @Disabled
        //this goes against requirements
    void whenEnrollUnder24HourBeforeIniDateThenShouldReturnTooLateException() {
        String emailCreator = ("amador@arribas.alf");
        String emailEnrolled = ("z@z.z");
        userService.saveUser(UserTestUtils.createUser(emailCreator));
        userService.saveUser(UserTestUtils.createUser(emailEnrolled));

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.HOUR_OF_DAY, 23);
        Date iniDate = cal.getTime();
        cal.add(Calendar.HOUR_OF_DAY, 1); //endDate

        Event event = eventService.saveEvent(EventTestUtils.createEvent(emailCreator, iniDate, cal.getTime()));

        assertThrows(TooLateException.class, () -> enrollmentService.saveEnrollment(event.getId(), emailEnrolled));
    }
}

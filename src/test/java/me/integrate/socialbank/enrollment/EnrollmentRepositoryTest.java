package me.integrate.socialbank.enrollment;


import me.integrate.socialbank.enrollment.exceptions.EnrollmentAlreadyExistsException;
import me.integrate.socialbank.event.Event;
import me.integrate.socialbank.event.EventRepository;
import me.integrate.socialbank.event.EventTestUtils;
import me.integrate.socialbank.user.User;
import me.integrate.socialbank.user.UserRepository;
import me.integrate.socialbank.user.UserTestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ExtendWith(SpringExtension.class)
public class EnrollmentRepositoryTest {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void givenEnrollmentThenIsStoredCorrectly() {
        String email = "a@a.com";
        User user = UserTestUtils.createUser(email);
        userRepository.saveUser(user);

        Event event = EventTestUtils.createEvent(email);
        int id = eventRepository.saveEvent(event).getId();

        Enrollment enrollment = new Enrollment(id, email);
        assertEquals(enrollment, enrollmentRepository.saveEnrollment(id, email));
    }

    @Test
    void givenEnrollmentsStoredInDatabaseWhenRetrievedByEventReturnEnrollments() {
        String emailCreator = "a@a.a", emailEnrolledOne = "z@z.z", emailEnrolledTwo = "c@c.c";

        userRepository.saveUser(UserTestUtils.createUser(emailCreator));
        userRepository.saveUser(UserTestUtils.createUser(emailEnrolledOne));
        userRepository.saveUser(UserTestUtils.createUser(emailEnrolledTwo));

        int id = eventRepository.saveEvent(EventTestUtils.createEvent(emailCreator)).getId();
        List<String> le = new ArrayList<>();
        le.add(emailEnrolledOne); le.add(emailEnrolledTwo);

        enrollmentRepository.saveEnrollment(id, emailEnrolledOne);
        enrollmentRepository.saveEnrollment(id, emailEnrolledTwo);

        List<String> retList = enrollmentRepository.getEnrollmentsOfEvent(id);
        assertTrue(le.containsAll(retList));
        assertTrue(retList.containsAll(le));
    }

    @Test
    void givenEnrollmentsStoredInDatabaseWhenRetrievedByUserReturnEnrollments() {
        String emailCreator = "a@a.a", emailEnrolled = "z@z.z";

        userRepository.saveUser(UserTestUtils.createUser(emailCreator));
        userRepository.saveUser(UserTestUtils.createUser(emailEnrolled));

        int idOne = eventRepository.saveEvent(EventTestUtils.createEvent(emailCreator)).getId();
        int idTwo = eventRepository.saveEvent(EventTestUtils.createEvent(emailCreator)).getId();
        List<Integer> le = new ArrayList<>();
        le.add(idOne); le.add(idTwo);

        enrollmentRepository.saveEnrollment(idOne, emailEnrolled);
        enrollmentRepository.saveEnrollment(idTwo, emailEnrolled);

        List<Integer> retList = enrollmentRepository.getEnrollmentsOfUser(emailEnrolled);
        assertTrue(le.containsAll(retList));
        assertTrue(retList.containsAll(le));
    }

    @Test
    void givenEnrollmentsStoredInDatabaseWhenSaveSameEnrollmentThenThrowException() {
        String emailCreator = "a@a.a", emailEnrolled = "z@z.z";

        userRepository.saveUser(UserTestUtils.createUser(emailCreator));
        userRepository.saveUser(UserTestUtils.createUser(emailEnrolled));

        int id = eventRepository.saveEvent(EventTestUtils.createEvent(emailCreator)).getId();

        enrollmentRepository.saveEnrollment(id, emailEnrolled);
        assertThrows(EnrollmentAlreadyExistsException.class, () -> enrollmentRepository.saveEnrollment(id,
                emailEnrolled));
    }

    @Test
    void givenEnrollmentStoredInDatabaseWhenDeletedThenIsNoLongerStored() {
        String creatorEmail = "email@email.tld";
        String enrolledEmail = "e@e.e";
        userRepository.saveUser(UserTestUtils.createUser(creatorEmail));
        userRepository.saveUser(UserTestUtils.createUser(enrolledEmail));
        int eventId = eventRepository.saveEvent(EventTestUtils.createEvent(creatorEmail)).getId();
        enrollmentRepository.saveEnrollment(eventId, enrolledEmail);
        enrollmentRepository.deleteEnrollment(eventId, enrolledEmail);
        List<String> emailsEnrolled = enrollmentRepository.getEnrollmentsOfEvent(eventId);
        assert (!emailsEnrolled.contains(enrolledEmail));
    }

}

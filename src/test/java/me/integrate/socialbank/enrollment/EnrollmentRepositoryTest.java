package me.integrate.socialbank.enrollment;


import me.integrate.socialbank.event.*;
import me.integrate.socialbank.user.*;
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

        Enrollment enrollment = new Enrollment(email, id);
        assertEquals(enrollment, enrollmentRepository.saveEnrollment(enrollment));
    }

    @Test
    void givenEnrollmentsStoredInDatabaseWhenRetrievedByEventReturnEnrollments() {
        String emailCreator = "a@a.a", emailEnrolledOne = "b@b.b", emailEnrolledTwo = "c@c.c";

        userRepository.saveUser(UserTestUtils.createUser(emailCreator));
        userRepository.saveUser(UserTestUtils.createUser(emailEnrolledOne));
        userRepository.saveUser(UserTestUtils.createUser(emailEnrolledTwo));

        int id = eventRepository.saveEvent(EventTestUtils.createEvent(emailCreator)).getId();
        List<String> le = new ArrayList<>();
        le.add(emailEnrolledOne); le.add(emailEnrolledTwo);

        enrollmentRepository.saveEnrollment(new Enrollment(emailEnrolledOne, id));
        enrollmentRepository.saveEnrollment(new Enrollment(emailEnrolledTwo, id));

        List<String> retList = enrollmentRepository.getEnrollmentsOfEvent(id);
        assertTrue(le.containsAll(retList));
        assertTrue(retList.containsAll(le));
    }

    @Test
    void givenEnrollmentsStoredInDatabaseWhenRetrievedByUserReturnEnrollments() {
        String emailCreator = "a@a.a", emailEnrolled = "b@b.b";

        userRepository.saveUser(UserTestUtils.createUser(emailCreator));
        userRepository.saveUser(UserTestUtils.createUser(emailEnrolled));

        int idOne = eventRepository.saveEvent(EventTestUtils.createEvent(emailCreator)).getId();
        int idTwo = eventRepository.saveEvent(EventTestUtils.createEvent(emailCreator)).getId();
        List<Integer> le = new ArrayList<>();
        le.add(idOne); le.add(idTwo);

        enrollmentRepository.saveEnrollment(new Enrollment(emailEnrolled, idOne));
        enrollmentRepository.saveEnrollment(new Enrollment(emailEnrolled, idTwo));

        List<Integer> retList = enrollmentRepository.getEnrollmentsOfUser(emailEnrolled);
        assertTrue(le.containsAll(retList));
        assertTrue(retList.containsAll(le));
    }

}

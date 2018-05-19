package me.integrate.socialbank.enrollment;


import me.integrate.socialbank.event.*;
import me.integrate.socialbank.user.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
}

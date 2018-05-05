package me.integrate.socialbank.event;

import me.integrate.socialbank.user.UserRepository;
import me.integrate.socialbank.user.UserTestUtils;
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
class EventRepositoryTest {
    @Autowired
    private EventRepositoryImpl eventRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void givenEventStoredInDatabaseWhenRetrievedByIdThenReturnsSameEvent() {
        String email = "pepito@pepito.com";
        userRepository.saveUser(UserTestUtils.createUser(email));
        Event event = eventRepository.saveEvent(EventTestUtils.createEvent(email));
        assertEquals(event, eventRepository.getEventById(event.getId()));
    }

    @Test
    void givenTwoDifferentEventsWhenSavedThenReturnSameEvents() {
        String email = "pepito@pepito.com";
        userRepository.saveUser(UserTestUtils.createUser(email));
        Event eventOne = eventRepository.saveEvent(EventTestUtils.createEvent(email));
        Event eventTwo = eventRepository.saveEvent(EventTestUtils.createEvent(email));

        assertEquals(eventOne, eventRepository.getEventById(eventOne.getId()));
        assertEquals(eventTwo, eventRepository.getEventById(eventTwo.getId()));
    }

    @Test
    void givenEventsStoredInDatabaseWhenRetrievedByEmailReturnSameEvents() {
        String email = "pepito@pepito.com";
        String otherEmail = "otheruser@other.com";
        userRepository.saveUser(UserTestUtils.createUser(email));
        userRepository.saveUser(UserTestUtils.createUser(otherEmail));
        Event eventOne = eventRepository.saveEvent(EventTestUtils.createEvent(email));
        Event otherEvent = eventRepository.saveEvent(EventTestUtils.createEvent(otherEmail));

        assertEquals(eventOne, eventRepository.getEventsByCreator(email).get(0));
    }

}

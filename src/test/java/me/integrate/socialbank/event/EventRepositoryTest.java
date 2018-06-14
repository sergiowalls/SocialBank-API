package me.integrate.socialbank.event;

import me.integrate.socialbank.user.UserRepository;
import me.integrate.socialbank.user.UserTestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    @Test
    void givenEventStoredInDatabaseWhenDeletedThenIsNoLongerStored() {
        String email = "email@email.tld";
        userRepository.saveUser(UserTestUtils.createUser(email));
        int eventId = eventRepository.saveEvent(EventTestUtils.createEvent(email)).getId();
        eventRepository.deleteEvent(eventId);
        Assertions.assertThrows(EventNotFoundException.class, () -> eventRepository.getEventById(eventId));
    }

    @Test
    void givenDifferentEventsStoredInDatabaseWhenDeletedOneThenTheOtherIsStillStored() {
        String email = "em@email.tld";
        userRepository.saveUser(UserTestUtils.createUser(email));
        Event eventOne = eventRepository.saveEvent(EventTestUtils.createEvent(email));
        Event eventTwo = eventRepository.saveEvent(EventTestUtils.createEvent(email));
        eventRepository.deleteEvent(eventOne.getId());
        assertEquals(eventTwo, eventRepository.getEventById(eventTwo.getId()));
        assertThrows(EventNotFoundException.class, () -> eventRepository.getEventById(eventOne.getId()));
    }

    @Test
    void givenEventsStoredWhenRetrievedByTagTheyAreCorrectlyReturned() {
        String email = "em@email.tld";
        userRepository.saveUser(UserTestUtils.createUser(email));


        Event event = eventRepository.saveEvent(EventTestUtils.createEvent(email));
        eventRepository.saveTags(event.getId(), Arrays.asList("tinder", "trap", "hoes", "andrea"));

        assertTrue(eventRepository.getEventsByTags(Arrays.asList("trap", "andrea", "faketag")).contains(event));
        assertFalse(eventRepository.getEventsByTags(Collections.singletonList("faketagfaketag")).contains(event));
    }
}

package me.integrate.socialbank.event;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static me.integrate.socialbank.event.EventTestUtils.sameEvent;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
@ExtendWith(SpringExtension.class)
public class EventRepositoryTest {
    @Autowired
    private EventRepository eventRepository;

    @Test
    void givenEventStoredInDatabaseWhenRetrievedByIdThenReturnsSameEvent() {
        Event event = eventRepository.saveEvent(EventTestUtils.createEvent());
        assertTrue(sameEvent(event, eventRepository.getEventById(event.getId())));
    }

    @Test
    void givenTwoDifferentEventsWhenSavedThenReturnSameEvents() {
        Event eventOne = eventRepository.saveEvent(EventTestUtils.createEvent());
        Event eventTwo = eventRepository.saveEvent(EventTestUtils.createEvent());

        assertTrue(sameEvent(eventOne, eventRepository.getEventById(eventOne.getId())));
        assertTrue(sameEvent(eventTwo, eventRepository.getEventById(eventTwo.getId())));
    }


}

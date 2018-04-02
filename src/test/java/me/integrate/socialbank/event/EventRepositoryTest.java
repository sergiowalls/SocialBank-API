package me.integrate.socialbank.event;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@ExtendWith(SpringExtension.class)
public class EventRepositoryTest {
    @Autowired
    private EventRepository eventRepository;

    @Test
    void givenEventStoredInDatabaseWhenRetrievedByIdThenReturnsSameEvent() {
        int id = 1;
        Event event = EventTestUtils.createEvent(1);
        eventRepository.saveEvent(event);

        assertEquals(event, eventRepository.getEventById(1));
    }

    @Test
    void givenTwoDifferentEventsWhenSavedThenReturnSameEvents() {
        int idOne = 1;
        int idTwo = 2;
        Event eventOne = EventTestUtils.createEvent(idOne);
        Event eventTwo = EventTestUtils.createEvent(idTwo);
        eventRepository.saveEvent(eventOne);
        eventRepository.saveEvent(eventTwo);

        assertEquals(eventOne, eventRepository.getEventById(idOne));
        assertEquals(eventTwo, eventRepository.getEventById(idTwo));

    }

    @Test
    public void givenEventWhenSavedTwiceThenThrowsException() {
        int id = 1;
        Event event = EventTestUtils.createEvent(id);
        eventRepository.saveEvent(event);

        assertThrows(EventAlreadyExistsException.class, () -> eventRepository.saveEvent(event)); //Duplicate primary key
    }
}

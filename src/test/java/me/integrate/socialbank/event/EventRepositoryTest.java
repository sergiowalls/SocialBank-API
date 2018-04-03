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
        Event event = eventRepository.saveEvent(EventTestUtils.createEvent());

        assertEquals(EventTestUtils.createEvent(), eventRepository.getEventById(event.getId()));
    }

    @Test
    void givenTwoDifferentEventsWhenSavedThenReturnSameEvents() {
        Event eventOne = eventRepository.saveEvent(EventTestUtils.createEvent());
        Event eventTwo = eventRepository.saveEvent(EventTestUtils.createEvent());

        assertEquals(EventTestUtils.createEvent(), eventRepository.getEventById(eventOne.getId()));
        assertEquals(EventTestUtils.createEvent(), eventRepository.getEventById(eventTwo.getId()));

    }

    @Test
    void givenEventWhenSavedTwiceThenThrowsException() {
        Event event = EventTestUtils.createEvent();
        eventRepository.saveEvent(event);

        assertThrows(EventAlreadyExistsException.class, () -> eventRepository.saveEvent(event)); //Duplicate primary key
    }

    @Disabled
    @Test
    void givenEventWithIniDateNotLesThanEndDateThenThrowsException() {
        Date iniDate, endDate;
        iniDate = endDate = new Date();
        try {
            iniDate = new SimpleDateFormat("yyyy-MM-dd").parse("2020-03-03");
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        try {
            endDate = new SimpleDateFormat("yyyy-MM-dd").parse("2019-03-03");
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        Event event = EventTestUtils.createEvent(iniDate, endDate);
        eventRepository.saveEvent(event);

        assertThrows(EventWithIncorrectDateException.class, () -> eventRepository.saveEvent(event));
    }

    @Disabled
    @Test
    void givenEventWithIniDateLesThanCurrentDateThenThrowsException() {
        Date iniDate, endDate;
        iniDate = endDate = new Date();
        try {
            iniDate = new SimpleDateFormat("yyyy-MM-dd").parse("1990-03-03");
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        try {
            endDate = new SimpleDateFormat("yyyy-MM-dd").parse("2019-03-03");
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        Event event = EventTestUtils.createEvent(iniDate, endDate);
        eventRepository.saveEvent(event);

        assertThrows(EventWithIncorrectDateException.class, () -> eventRepository.saveEvent(event));
    }
}

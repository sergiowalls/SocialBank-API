package me.integrate.socialbank.event;

import jdk.nashorn.internal.ir.annotations.Ignore;
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
        int id = 1;
        Event event = EventTestUtils.createEvent(id);
        eventRepository.saveEvent(event);

        assertEquals(event, eventRepository.getEventById(id));
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
    void givenEventWhenSavedTwiceThenThrowsException() {
        int id = 1;
        Event event = EventTestUtils.createEvent(id);
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
        Event event = EventTestUtils.createEvent(1, iniDate, endDate);
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
        Event event = EventTestUtils.createEvent(1, iniDate, endDate);
        eventRepository.saveEvent(event);

        assertThrows(EventWithIncorrectDateException.class, () -> eventRepository.saveEvent(event));
    }
}

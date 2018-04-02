package me.integrate.socialbank.event;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static me.integrate.socialbank.event.EventTestUtils.createEvent;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@ExtendWith(SpringExtension.class)
public class EventServiceTest {

    @Autowired
    EventService eventService;

    @Test
    void givenEventWhenSaveItThenReturnsSameEvent() {
        int id = 123;
        Event event = createEvent(id);
        Event savedEvent = eventService.saveEvent(event);

        assertEquals(event, savedEvent);

    }
}

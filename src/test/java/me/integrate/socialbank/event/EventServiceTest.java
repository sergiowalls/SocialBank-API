package me.integrate.socialbank.event;

import me.integrate.socialbank.user.UserRepository;
import me.integrate.socialbank.user.UserTestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static me.integrate.socialbank.event.EventTestUtils.createEvent;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
@ExtendWith(SpringExtension.class)
public class EventServiceTest {

    @Autowired
    EventService eventService;

    @Autowired
    UserRepository userRepository;

    @Test
    void givenEventWhenSaveItThenReturnsSameEvent() {

        String email = "pepito@pepito.com";
        userRepository.saveUser(UserTestUtils.createUser(email));
        Event event = createEvent(email);
        Event savedEvent = eventService.saveEvent(event);

        assertEquals(event, savedEvent);
    }

    @Test
    void givenEventsWhenSaveItThenReturnListOfThem() {
        String email = "pepito@pepito.com";
        userRepository.saveUser(UserTestUtils.createUser(email));
        Event event = eventService.saveEvent(createEvent(email));
        Event event2 = eventService.saveEvent(createEvent(email));

        List<Event> returnList = eventService.getAllEvents();

        assertTrue(returnList.contains(event));
        assertTrue(returnList.contains(event2));
    }

    @Test
    void givenEventWithoutDateWhenSaveItThenReturnsSameEvent() {

        String email = "pepito@pepito.com";
        userRepository.saveUser(UserTestUtils.createUser(email));
        Event event = createEvent(email, null, null);
        Event savedEvent = eventService.saveEvent(event);

        assertEquals(event, savedEvent);

    }


}

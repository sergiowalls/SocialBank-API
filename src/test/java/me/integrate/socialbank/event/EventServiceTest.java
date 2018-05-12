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
import static org.junit.jupiter.api.Assertions.*;

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
    void givenEventsOfSameCategoryWhenGetByCategoryThenReturnsBoth() {
        String email = "pepito@pepito.com";
        userRepository.saveUser(UserTestUtils.createUser(email));
        Event event = eventService.saveEvent(createEvent(email, Category.CULTURE));
        Event event2 = eventService.saveEvent(createEvent(email, Category.CULTURE));

        List<Event> events = eventService.getEventsByCategory(Category.CULTURE);

        assertTrue(events.contains(event));
        assertTrue(events.contains(event2));
    }

    @Test
    void givenEventsOfDifferentCategoriesWhenGetByCategoryThenReturnsOnlyOne() {
        String email = "pepito@pepito.com";
        userRepository.saveUser(UserTestUtils.createUser(email));
        Event event = eventService.saveEvent(createEvent(email, Category.CULTURE));
        Event event2 = eventService.saveEvent(createEvent(email, Category.GASTRONOMY));

        List<Event> events = eventService.getEventsByCategory(Category.CULTURE);

        assertTrue(events.contains(event));
        assertFalse(events.contains(event2));
    }

    @Test
    void givenEventsOfDifferentCategoriesWhenGetByAnotherCategoryThenReturnsEmptyList() {
        String email = "pepito@pepito.com";
        userRepository.saveUser(UserTestUtils.createUser(email));
        eventService.saveEvent(createEvent(email, Category.CULTURE));
        eventService.saveEvent(createEvent(email, Category.GASTRONOMY));

        List<Event> events = eventService.getEventsByCategory(Category.LEISURE);

        assertTrue(events.isEmpty());
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

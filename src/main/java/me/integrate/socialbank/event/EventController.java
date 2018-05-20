package me.integrate.socialbank.event;

import me.integrate.socialbank.user.User;
import me.integrate.socialbank.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
public class EventController {
    private EventService eventService;
    private UserService userService;

    @Autowired
    public EventController(EventService eventService, UserService userService) {
        this.eventService = eventService;
        this.userService = userService;
    }

    @GetMapping("/events/{id}")
    public Event getEventById(@PathVariable int id) {
        return eventService.getEventById(id);
    }

    @PutMapping("/events/{id}")
    public Event updateEvent(@PathVariable int id, @RequestBody Event event) {
        return eventService.updateEvent(id, event);
    }

    @PostMapping("/events")
    @ResponseStatus(HttpStatus.CREATED)
    public Event saveEvent(@RequestBody Event event, Authentication authentication) {
        event.setCreatorEmail(authentication.getName());
        if (event.getIniDate() != null && event.getEndDate() != null && (event.getIniDate().after(event.getEndDate()) || event.getIniDate().before(new Date()))) throw new EventWithIncorrectDateException();
        if (event.isDemand()) {
            long diff = Math.abs(event.getIniDate().getTime() - event.getEndDate().getTime());
            diff = diff / (60 * 60 * 1000);
            User user = userService.getUserByEmail(authentication.getName());
            if (diff > user.getBalance()) throw new UserNotEnoughHoursException();
        }
        return eventService.saveEvent(event);
    }

    @GetMapping("/events")
    public @ResponseBody
    List<Event> getAllEvents(@RequestParam(value = "category", required = false) Category category) {
        if (category != null) return eventService.getEventsByCategory(category);
        return eventService.getAllEvents();
    }

    @GetMapping("/users/{emailCreator}/events")
    public @ResponseBody
    List<Event> getEventsByCreator(@PathVariable String emailCreator) {
        return eventService.getEventsByCreator(emailCreator);
    }

    @DeleteMapping("/events/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteEvent(@PathVariable int id) {
        eventService.deleteEvent(id);
    }

}

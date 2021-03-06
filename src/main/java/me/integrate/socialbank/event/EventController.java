package me.integrate.socialbank.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EventController {
    private EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/events/{id}")
    public Event getEventById(@PathVariable int id, Authentication authentication) {
        return eventService.getEventById(id, authentication.getName());
    }

    @PutMapping("/events/{id}")
    public Event updateEvent(@PathVariable int id, @RequestBody Event event) {
        return eventService.updateEvent(id, event);
    }

    @PostMapping("/events")
    @ResponseStatus(HttpStatus.CREATED)
    public Event saveEvent(@RequestBody Event event, Authentication authentication) {
        event.setCreatorEmail(authentication.getName());
        return eventService.saveEvent(event);
    }

    @GetMapping("/events")
    public @ResponseBody
    List<Event> getAllEvents(@RequestParam(value = "category", required = false) Category category,
                            @RequestParam(value = "tags", required = false) List<String> tags) {
        return eventService.getEvents(category, tags);
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

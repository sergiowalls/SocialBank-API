package me.integrate.socialbank.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
public class EventController {
    private EventService eventService;

    @Autowired
    public EventController(EventService eventService) { this.eventService = eventService; }

    @GetMapping("/events/{id}")
    public Event getEventById(@PathVariable int id) {
        return eventService.getEventById(id);
    }

    @PostMapping("/events")
    @ResponseStatus(HttpStatus.CREATED)
    public Event saveEvent(@RequestBody Event event) {
        if (event.getIniDate().after(event.getEndDate()) || event.getIniDate().before(new Date())) throw new EventWithIncorrectDateException();
        return eventService.saveEvent(event);
    }

}

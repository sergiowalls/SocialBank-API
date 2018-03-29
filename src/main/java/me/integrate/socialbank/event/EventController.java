package me.integrate.socialbank.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class EventController {
    EventService eventService;

    @Autowired
    public EventController(EventService eventService) { this.eventService = eventService; }

    @GetMapping("/events/{id}")
    public Event getEventByEmail(@PathVariable int id) { return eventService.getEventById(id); }

    @PostMapping("/events")
    @ResponseStatus(HttpStatus.CREATED)
    public Event saveEvent(@RequestBody Event event) { return eventService.saveEvent(event);}

}

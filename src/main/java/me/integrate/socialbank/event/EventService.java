package me.integrate.socialbank.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventService
{
    EventRepository eventRepository;

    @Autowired
    public EventService(EventRepository eventRepository) { this.eventRepository = eventRepository; }

    public Event getEventById(int id) { return eventRepository.getEventById(id); }

    public Event saveEvent(Event event) { return eventRepository.saveEvent(event);}
}

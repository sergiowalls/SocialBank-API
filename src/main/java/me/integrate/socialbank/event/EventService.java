package me.integrate.socialbank.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {
    private EventRepository eventRepository;

    @Autowired
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Event getEventById(int id) {
        return eventRepository.getEventById(id);
    }

    public Event saveEvent(Event event) {
        return eventRepository.saveEvent(event);
    }

    public List<Event> getEvents() { return eventRepository.getEvents(); }
}

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

    public List<Event> getAllEvents() { return eventRepository.getAllEvents(); }

    public List<Event> getEventsByCreator(String email) { return eventRepository.getEventsByCreator(email); }

    public List<Event> getEventsByCategory(Category category) {
        return eventRepository.getEventsByCategory(category);
    }

    public Event updateEvent(int id, Event event) {
        Event eventById = eventRepository.getEventById(id);
        if (eventById == null) throw new EventNotFoundException();
        eventRepository.updateEvent(id, event);
        return eventRepository.getEventById(id);
    }

    public Event deleteEvent(int id) {
        Event eventById = eventRepository.getEventById(id);
        if (eventById == null) throw new EventNotFoundException();
        eventRepository.deleteEvent(id);
        return eventById;
    }
}

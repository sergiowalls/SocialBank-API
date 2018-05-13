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

    public void deteleEvent(int id) { eventRepository.deleteEvent(id); }
}

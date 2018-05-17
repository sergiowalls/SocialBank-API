package me.integrate.socialbank.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
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
        Event event = eventRepository.getEventById(id);

        if (event == null) throw new EventNotFoundException();

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_MONTH, 1);
        if (event.getIniDate().before(cal.getTime())) throw new TooLateException();

        eventRepository.deleteEvent(id);
        return event;
    }
}

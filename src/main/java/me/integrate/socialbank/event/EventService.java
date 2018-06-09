package me.integrate.socialbank.event;

import me.integrate.socialbank.user.User;
import me.integrate.socialbank.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class EventService {
    private EventRepository eventRepository;
    private UserService userService;

    @Autowired
    public EventService(EventRepository eventRepository, UserService userService) {
        this.eventRepository = eventRepository;
        this.userService = userService;
    }

    public Event getEventById(int id) {
        return eventRepository.getEventById(id);
    }

    public Event saveEvent(Event event) {
        if (eventWithDates(event)) {
            if (event.getIniDate().after(event.getEndDate()) || event.getIniDate().before(new Date()))
                throw new EventWithIncorrectDateException();
            if (event.isDemand()) {
                User user = userService.getUserByEmail(event.getCreatorEmail());
                if (event.getIntervalTime() > user.getBalance()) throw new UserNotEnoughHoursException();
            }
        }
        return eventRepository.saveEvent(event);
    }

    public List<Event> getAllEvents() {
        return eventRepository.getAllEvents();
    }

    public List<Event> getEventsByCreator(String email) {
        return eventRepository.getEventsByCreator(email);
    }

    public List<Event> getEventsByCategory(Category category) {
        return eventRepository.getEventsByCategory(category);
    }

    public Event updateEvent(int id, Event event) {
        Event eventById = eventRepository.getEventById(id);
        if (eventById == null) throw new EventNotFoundException();
        eventRepository.updateEvent(id, event);
        return this.getEventById(id);
    }

    public Event incrementNumberEnrolled(int id) {
        eventRepository.incrementNumberEnrolled(id);
        return this.getEventById(id);
    }

    public Event decrementNumberEnrolled(int id) {
        eventRepository.decrementNumberEnrolled(id);
        return this.getEventById(id);
    }

    public Event deleteEvent(int id) {
        Event event = eventRepository.getEventById(id);

        if (event == null) throw new EventNotFoundException();

        if (event.beginsInLessThan24h()) throw new TooLateException();

        eventRepository.deleteEvent(id);
        return event;
    }

    private boolean eventWithDates(Event event) {
        return event.getIniDate() != null && event.getEndDate() != null;
    }
}

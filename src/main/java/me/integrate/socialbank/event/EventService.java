package me.integrate.socialbank.event;

import me.integrate.socialbank.enrollment.EnrollmentService;
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
    private EnrollmentService enrollmentService;

    @Autowired
    public EventService(EventRepository eventRepository, UserService userService, EnrollmentService enrollmentService) {
        this.eventRepository = eventRepository;
        this.userService = userService;
        this.enrollmentService = enrollmentService;
    }

    public Event getEventById(int id) {
        Event eventById = eventRepository.getEventById(id);
        eventById.setNumberEnrolled(enrollmentService.getNumberOfUsersEnrolledInEvent(id));
        return eventById;
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
        List<Event> events = eventRepository.getAllEvents();
        setNumberEnrolled(events);
        return events;
    }

    public List<Event> getEventsByCreator(String email) {
        List<Event> eventsByCreator = eventRepository.getEventsByCreator(email);
        setNumberEnrolled(eventsByCreator);
        return eventsByCreator;
    }

    public List<Event> getEventsByCategory(Category category) {
        List<Event> eventsByCategory = eventRepository.getEventsByCategory(category);
        setNumberEnrolled(eventsByCategory);
        return eventsByCategory;
    }

    public Event updateEvent(int id, Event event) {
        Event eventById = eventRepository.getEventById(id);
        if (eventById == null) throw new EventNotFoundException();
        eventRepository.updateEvent(id, event);
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

    private void setNumberEnrolled(List<Event> events) {
        for (Event event : events) {
            int numberEnrolled = enrollmentService.getNumberOfUsersEnrolledInEvent(event.getId());
            event.setNumberEnrolled(numberEnrolled);
        }
    }
}

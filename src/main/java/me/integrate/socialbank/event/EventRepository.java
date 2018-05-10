package me.integrate.socialbank.event;

import java.util.List;

public interface EventRepository {

    Event saveEvent(Event event);

    Event getEventById(int id);

    void deleteEvent(int id);

    List<Event> getAllEvents();

    List<Event> getEventsByCreator(String email);
}

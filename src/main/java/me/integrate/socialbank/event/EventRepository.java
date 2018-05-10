package me.integrate.socialbank.event;

import java.util.List;

public interface EventRepository {

    Event saveEvent(Event event);

    Event getEventById(int id);

    List<Event> getEvents();

    void deleteEvent(int id);
}

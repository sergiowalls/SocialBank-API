package me.integrate.socialbank.event;

public interface EventRepository {

    Event saveEvent(Event event);

    Event getEventById(int id);
}

package me.integrate.socialbank.event;

import java.text.ParseException;
import java.text.SimpleDateFormat;

class EventTestUtils {

    static Event createEvent(int id) {
        return createEvent(id, "pepito@pepito.com");
    }

    static Event createEvent(int id, String email) {
        Event event = new Event();
        event.setId(id);
        event.setCreatorEmail(email);
        event.setHours(13);
        try {
            event.setIniDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-03-03"));
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        try {
            event.setEndDate(new SimpleDateFormat("yyyy-MM-dd").parse("2020-03-03"));
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        event.setLocation("53.2734, -7.778320310000026");
        event.setDescription("Worldwide Meeting");
        event.setTitle("Worldwide Meeting");
        return event;
    }
}

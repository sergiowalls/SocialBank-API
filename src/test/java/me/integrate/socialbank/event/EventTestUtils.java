package me.integrate.socialbank.event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

class EventTestUtils {

    static Event createEvent(int id) {
        Date iniDate, endDate;
        iniDate = endDate = new Date();
        try {
            iniDate = new SimpleDateFormat("yyyy-MM-dd").parse("2019-03-03");
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        try {
           endDate = new SimpleDateFormat("yyyy-MM-dd").parse("2020-03-03");
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return createEvent(id, iniDate, endDate);
    }

    static Event createEvent(int id, Date iniDate, Date endDate) {
        Event event = new Event();
        event.setId(id);
        event.setCreatorEmail("pepito@peito.com");
        event.setHours(13);
        event.setIniDate(iniDate);
        event.setEndDate(endDate);
        event.setLocation("53.2734, -7.778320310000026");
        event.setDescription("Worldwide Meeting");
        event.setTitle("Worldwide Meeting");
        return event;
    }
}

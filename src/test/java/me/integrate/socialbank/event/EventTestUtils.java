package me.integrate.socialbank.event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

class EventTestUtils {

    static Event createEvent() {
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
        return createEvent(iniDate, endDate);
    }

    static Event createEvent(Date iniDate, Date endDate) {
        Event event = new Event();
        event.setCreatorEmail("pepito@peito.com");
        event.setHours(13);
        event.setIniDate(iniDate);
        event.setEndDate(endDate);
        event.setLocation("53.2734, -7.778320310000026");
        event.setDescription("Worldwide Meeting");
        event.setTitle("Worldwide Meeting");
        return event;
    }

    //event null != event null
    static boolean sameEvent(Event a, Event b) {
        return  a != null && b != null &&
                Objects.equals(a.getCreatorEmail(), b.getCreatorEmail()) &&
                Objects.equals(a.getIniDate(), b.getIniDate()) &&
                Objects.equals(a.getEndDate(), b.getEndDate()) &&
                a.getHours() == b.getHours() &&
                Objects.equals(a.getLocation(), b.getLocation()) &&
                Objects.equals(a.getTitle(), b.getTitle()) &&
                Objects.equals(a.getDescription(), b.getDescription());
    }
}

package me.integrate.socialbank.event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

class EventTestUtils {

    private static String INI_DATE = "2019-03-03";
    private static String END_DATE = "2020-03-03";
    private static String EMAIL = "pepito@pepito.com";

    private static Date getDate(String inputDate) {
        Date date = new Date();
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(inputDate);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return date;
    }

    static Event createEvent() {
        return createEvent(EMAIL,getDate(INI_DATE),  getDate(END_DATE));
    }

    static Event createEvent(String email) {
        return createEvent(email,getDate(INI_DATE), getDate(END_DATE));
    }

    static Event createEvent(Date iniDate, Date endDate) {
        return createEvent(EMAIL,iniDate, endDate);
    }

    static Event createEvent(String email, Date iniDate, Date endDate) {
        Event event = new Event();
        event.setCreatorEmail(email);
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

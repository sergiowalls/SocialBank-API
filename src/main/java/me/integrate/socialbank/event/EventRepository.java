package me.integrate.socialbank.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class EventRepository {

    public static String EVENT_TABLE = "\"event\"";
    public static String ID = "id";
    public static String CREATOR = "creatorEmail";
    public static String INIDATE = "iniDate";
    public static String ENDDATE = "endDate";
    public static String HOURS = "hours";
    public static String LOCATION = "location";
    public static String TITLE = "title";
    public static String DESCRIPTION = "description";

    private JdbcTemplate jdbcTemplate;

    private class EventRowMapper implements RowMapper<Event>
    {
        @Override
        public Event mapRow(ResultSet resultSet, int i) throws SQLException
        {

            Event event = new Event();
            event.setId(resultSet.getInt(ID));
            event.setCreatorEmail(resultSet.getString(CREATOR));
            event.setIniDate(resultSet.getDate(INIDATE));
            event.setIniDate(resultSet.getDate(ENDDATE));
            event.setHours(resultSet.getInt(HOURS));
            event.setLocation(resultSet.getString(LOCATION));
            event.setTitle(resultSet.getString(TITLE));
            event.setDescription(resultSet.getString(DESCRIPTION));

            return event;
        }
    }

    @Autowired
    public EventRepository(JdbcTemplate jdbcTemplate) {this.jdbcTemplate = jdbcTemplate; }

    public Event getEventById(int id) {
        return jdbcTemplate.queryForObject("SELECT * FROM " + EVENT_TABLE + " WHERE " + ID + "= ?",
                new Object[]{id}, new EventRowMapper());
    }

    public Event saveEvent(Event event)
    {
        try
        {
            jdbcTemplate.update("INSERT INTO" + EVENT_TABLE + " VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                    event.getId(), event.getCreatorEmail(), event.getIniDate(), event.getEndDate(),
                    event.getHours(), event.getLocation(), event.getTitle(), event.getDescription());
        }
        catch (DuplicateKeyException ex)
        {
            throw new EventAlreadyExistsException();
        }
        return event;
    }
}

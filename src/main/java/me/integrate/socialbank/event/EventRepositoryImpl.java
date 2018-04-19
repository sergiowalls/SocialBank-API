package me.integrate.socialbank.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class EventRepositoryImpl implements EventRepository {

    private static String EVENT_TABLE = "event";
    private static String ID = "id";
    private static String CREATOR = "creatorEmail";
    private static String INIDATE = "iniDate";
    private static String ENDDATE = "endDate";
    private static String HOURS = "hours";
    private static String LOCATION = "location";
    private static String TITLE = "title";
    private static String DESCRIPTION = "description";
    private final SimpleJdbcInsert simpleJdbcInsert;

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public EventRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(this.jdbcTemplate)
                .withTableName(EVENT_TABLE)
                .usingColumns(CREATOR, INIDATE, ENDDATE, HOURS, LOCATION, TITLE, DESCRIPTION)
                .usingGeneratedKeyColumns(ID);
    }

    public Event saveEvent(Event event) {
        Map<String, Object> params = new HashMap<>();
        params.put(CREATOR, event.getCreatorEmail());
        params.put(INIDATE, event.getIniDate());
        params.put(ENDDATE, event.getEndDate());
        params.put(HOURS, event.getHours());
        params.put(LOCATION, event.getLocation());
        params.put(TITLE, event.getTitle());
        params.put(DESCRIPTION, event.getDescription());
        Number id = this.simpleJdbcInsert.executeAndReturnKey(params);
        event.setId(id.intValue());
        return event;
    }

    public Event getEventById(int id) {
        return jdbcTemplate.queryForObject("SELECT * FROM " + EVENT_TABLE + " WHERE " + ID + "= ?",
                new Object[]{id}, new EventRowMapper());
    }

    public List<Event> getEvents() {
        return jdbcTemplate.query("SELECT * FROM " + EVENT_TABLE, new EventRowMapper());
    }

    private class EventRowMapper implements RowMapper<Event> {
        @Override
        public Event mapRow(ResultSet resultSet, int i) throws SQLException {

            Event event = new Event();
            event.setId(resultSet.getInt(ID));
            event.setCreatorEmail(resultSet.getString(CREATOR));
            event.setIniDate(resultSet.getDate(INIDATE));
            event.setEndDate(resultSet.getDate(ENDDATE));
            event.setHours(resultSet.getInt(HOURS));
            event.setLocation(resultSet.getString(LOCATION));
            event.setTitle(resultSet.getString(TITLE));
            event.setDescription(resultSet.getString(DESCRIPTION));

            return event;
        }
    }
}

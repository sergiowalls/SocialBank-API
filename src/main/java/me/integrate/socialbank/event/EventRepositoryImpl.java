package me.integrate.socialbank.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class EventRepositoryImpl implements EventRepository {

    private static String EVENT_TABLE = "event";
    private static String EVENT_TAGS_TABLE = "event_tags";
    private static String ID = "id";
    private static String CREATOR = "creatorEmail";
    private static String INIDATE = "iniDate";
    private static String ENDDATE = "endDate";
    private static String LOCATION = "location";
    private static String TITLE = "title";
    private static String DESCRIPTION = "description";
    private static String IMAGE = "image";
    private static String ISDEMAND = "isDemand";
    private static String LATITUDE = "latitude";
    private static String LONGITUDE = "longitude";
    private static String CATEGORY = "category";
    private static String CAPACITY = "capacity";
    private static String NUMBER_ENROLLED = "number_enrolled";
    private static String EVENT_ID = "event_id";
    private static String TAG = "tag";
    private final SimpleJdbcInsert simpleJdbcInsert;

    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public EventRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        this.simpleJdbcInsert = new SimpleJdbcInsert(this.jdbcTemplate)
                .withTableName(EVENT_TABLE).usingColumns(CREATOR, INIDATE, ENDDATE, LOCATION, TITLE, DESCRIPTION,
                        IMAGE, ISDEMAND, LATITUDE, LONGITUDE, CATEGORY, CAPACITY, NUMBER_ENROLLED)
                .usingGeneratedKeyColumns(ID);
    }

    public Event saveEvent(Event event) {
        Map<String, Object> params = new HashMap<>();
        params.put(CREATOR, event.getCreatorEmail());
        params.put(INIDATE, event.getIniDate());
        params.put(ENDDATE, event.getEndDate());
        params.put(LOCATION, event.getLocation());
        params.put(TITLE, event.getTitle());
        params.put(DESCRIPTION, event.getDescription());
        params.put(IMAGE, event.getImage());
        params.put(ISDEMAND, event.isDemand());
        params.put(LATITUDE, event.getLatitude());
        params.put(LONGITUDE, event.getLongitude());
        params.put(CATEGORY, event.getCategory().name());
        params.put(CAPACITY, event.getCapacity());
        params.put(NUMBER_ENROLLED, 0);
        Number id = this.simpleJdbcInsert.executeAndReturnKey(params);
        event.setId(id.intValue());
        return event;
    }

    public void saveTags(int id, List<String> tags) {
        for (String tag : tags) {
            jdbcTemplate.update("INSERT INTO " + EVENT_TAGS_TABLE + " VALUES(?, ?)",
                    id, tag);
        }
    }

    public Event getEventById(int id) {
        Event event;
        try {
            event = jdbcTemplate.queryForObject("SELECT * FROM " + EVENT_TABLE + " WHERE " + ID + "= ?",
                    new Object[]{id}, new EventRowMapper());
        } catch (EmptyResultDataAccessException e) {
            throw new EventNotFoundException();
        }
        return event;
    }

    public void updateEvent(int id, Event event) {
        String sql = "UPDATE " + EVENT_TABLE + " SET " + IMAGE + " = ?, " + DESCRIPTION + " = ? WHERE " + ID + " = ?";
        jdbcTemplate.update(sql, event.getImage(), event.getDescription(), id);
    }

    @Override
    public void incrementNumberEnrolled(int id) {
        Event event = jdbcTemplate.queryForObject("SELECT * FROM " + EVENT_TABLE + " WHERE " + ID + "= ?", new
                Object[]{id}, new EventRowMapper());
        String sql = "UPDATE " + EVENT_TABLE + " SET " + NUMBER_ENROLLED + " = ? WHERE " + ID + " = ?";
        jdbcTemplate.update(sql, event.getNumberEnrolled() + 1, id);
    }

    @Override
    public void decrementNumberEnrolled(int id) {
        Event event = jdbcTemplate.queryForObject("SELECT * FROM " + EVENT_TABLE + " WHERE " + ID + "= ?", new
                Object[]{id}, new EventRowMapper());
        String sql = "UPDATE " + EVENT_TABLE + " SET " + NUMBER_ENROLLED + " = ? WHERE " + ID + " = ?";
        jdbcTemplate.update(sql, event.getNumberEnrolled() - 1, id);
    }

    public List<Event> getEventsByCreator(String email) {
        return jdbcTemplate.query("SELECT * FROM " + EVENT_TABLE + " WHERE " + CREATOR + "= ?",
                new Object[]{email}, new EventRowMapper());
    }

    public List<Event> getEventsByCategory(Category category) {
        return jdbcTemplate.query("SELECT * FROM " + EVENT_TABLE + " WHERE " + CATEGORY + "= ?",
                new Object[]{category.name()}, new EventRowMapper());
    }

    public List<Event> getEventsByTags(List<String> tags) {
        String query = "SELECT * FROM " + EVENT_TABLE + " e WHERE EXISTS ( SELECT * FROM " + EVENT_TAGS_TABLE
                + " et WHERE e." + ID + " = et." + EVENT_ID + " AND et." + TAG + " IN (:tags))";
        Map namedParameters = Collections.singletonMap("tags", tags);

        return namedParameterJdbcTemplate.query(query, namedParameters, new EventRowMapper());
    }

    public List<Event> getAllEvents() {
        return jdbcTemplate.query("SELECT * FROM " + EVENT_TABLE, new EventRowMapper());
    }

    public void deleteEvent(int id) {
        jdbcTemplate.update("DELETE FROM " + EVENT_TABLE + " WHERE " + ID + "=?", id);
    }

    private class EventRowMapper implements RowMapper<Event> {
        @Override
        public Event mapRow(ResultSet resultSet, int i) throws SQLException {

            Event event = new Event();
            event.setId(resultSet.getInt(ID));
            event.setCreatorEmail(resultSet.getString(CREATOR));
            event.setIniDate(resultSet.getTimestamp(INIDATE));
            event.setEndDate(resultSet.getTimestamp(ENDDATE));
            event.setLocation(resultSet.getString(LOCATION));
            event.setTitle(resultSet.getString(TITLE));
            event.setDescription(resultSet.getString(DESCRIPTION));
            event.setImage(resultSet.getString(IMAGE));
            event.setDemand(resultSet.getBoolean(ISDEMAND));
            event.setLatitude(resultSet.getDouble(LATITUDE));
            event.setLongitude(resultSet.getDouble(LONGITUDE));
            event.setCategory(Category.valueOf(resultSet.getString(CATEGORY)));
            event.setCapacity((Integer) resultSet.getObject(CAPACITY));
            event.setNumberEnrolled(resultSet.getInt(NUMBER_ENROLLED));
            return event;
        }
    }
}

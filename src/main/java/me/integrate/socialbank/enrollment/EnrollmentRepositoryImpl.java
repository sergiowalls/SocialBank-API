package me.integrate.socialbank.enrollment;

import me.integrate.socialbank.enrollment.exceptions.EnrollmentAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class EnrollmentRepositoryImpl implements EnrollmentRepository{

    private static String ENROLLMENT_TABLE ="user_event_enrollment";
    private static String USEREMAIL = "user_email";
    private static String EVENTID = "event_id";

    private final SimpleJdbcInsert simpleJdbcInsert;

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public EnrollmentRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(this.jdbcTemplate)
                .withTableName(ENROLLMENT_TABLE)
                .usingColumns(EVENTID, USEREMAIL);
    }

    @Override
    public Enrollment saveEnrollment(Enrollment enrollment) {
        try {
            jdbcTemplate.update("INSERT INTO " + ENROLLMENT_TABLE + " VALUES (?, ?)", enrollment.getUserEmail(),
                    enrollment.getEventId());
        } catch (DuplicateKeyException ex) {
            throw new EnrollmentAlreadyExistsException();
        }

        return enrollment;
    }

    @Override
    public List<String> getEnrollmentsOfEvent(int id) {
        return jdbcTemplate.queryForList("SELECT " + USEREMAIL + " FROM " + ENROLLMENT_TABLE + " WHERE " + EVENTID + "= ?",
                new Object[]{id}, String.class);
    }

    @Override
    public List<Integer> getEnrollmentsOfUser(String email) {
        return jdbcTemplate.queryForList("SELECT " + EVENTID + " FROM " + ENROLLMENT_TABLE + " WHERE " + USEREMAIL + "= ?",
                new Object[]{email},Integer.class);
    }

    private class EnrollmentRowMapper implements RowMapper<Enrollment> {
        @Override
        public Enrollment mapRow(ResultSet resultSet, int i) throws SQLException {
            Enrollment enrollment = new Enrollment();
            enrollment.setUserEmail(resultSet.getString(USEREMAIL));
            enrollment.setEventId(resultSet.getInt(EVENTID));
            return enrollment;
        }
    }
}

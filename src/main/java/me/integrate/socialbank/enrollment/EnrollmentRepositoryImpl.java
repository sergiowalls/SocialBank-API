package me.integrate.socialbank.enrollment;

import me.integrate.socialbank.enrollment.exceptions.EnrollmentAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

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
    public Enrollment enrollUserEvent(Enrollment enrollment) {
        try {
            jdbcTemplate.update("INSERT INTO " + ENROLLMENT_TABLE + " VALUES (?, ?)", enrollment.getUserEmail(),
                    enrollment.getEventId());
        } catch (DuplicateKeyException ex) {
            throw new EnrollmentAlreadyExistsException();
        }

        return enrollment;
    }
}

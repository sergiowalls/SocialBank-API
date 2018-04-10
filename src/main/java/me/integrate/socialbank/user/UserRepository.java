package me.integrate.socialbank.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class UserRepository {
    private final static String USER_TABLE = "\"user\"";
    private final static String EMAIL = "email";
    private final static String NAME = "name";
    private final static String SURNAME = "surname";
    private final static String PASSWORD = "password";
    private final static String BIRTHDATE = "birthdate";
    private final static String GENDER = "gender";
    private final static String BALANCE = "balance";
    private final static String DESCRIPTION = "description";
    private final static String RECOVERY = "recovery";

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public User getUserByEmail(String email) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM " + USER_TABLE + " WHERE " + EMAIL + "= ?",
                    new Object[]{email}, new UserRowMapper());
        } catch (EmptyResultDataAccessException ex) {
            throw new UserNotFoundException();
        }
    }

    public User saveUser(User user) {
        try {
            jdbcTemplate.update("INSERT INTO " + USER_TABLE + " VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                    user.getEmail(), user.getName(), user.getSurname(), user.getPassword(), user.getBirthdate(),
                    user.getGender().toString(), user.getBalance(), user.getDescription());
        } catch (DuplicateKeyException ex) {
            throw new EmailAlreadyExistsException();
        }

        return user;
    }

    public void updatePassword(String email, String password) {
        jdbcTemplate.update("UPDATE " + USER_TABLE + " SET " + PASSWORD + " = ? WHERE " + EMAIL + " = ?",
                password, email);
    }

    public void updateRecoveryToken(String email, String recoveryToken) {
        jdbcTemplate.update("UPDATE " + USER_TABLE + " SET " + RECOVERY + " = ? WHERE " + EMAIL + " = ?",
                recoveryToken, email);
    }

    public String getEmailFromToken(String token) {
        String email;
        try {
            email = jdbcTemplate.queryForObject("SELECT email FROM " + USER_TABLE + " WHERE " + RECOVERY + "= ?",
                    new Object[]{token}, String.class);
        } catch (EmptyResultDataAccessException ex) {
            throw new UserNotFoundException();
        }

        return email;
    }

    private class UserRowMapper implements RowMapper<User> {

        @Override
        public User mapRow(ResultSet resultSet, int i) throws SQLException {
            User user = new User();
            user.setEmail(resultSet.getString(EMAIL));
            user.setName(resultSet.getString(NAME));
            user.setSurname(resultSet.getString(SURNAME));
            user.setPassword(resultSet.getString(PASSWORD));
            user.setBirthdate(resultSet.getDate(BIRTHDATE));
            user.setGender(User.Gender.valueOf(resultSet.getString(GENDER)));
            user.setBalance(resultSet.getFloat(BALANCE));
            user.setDescription(resultSet.getString(DESCRIPTION));

            return user;
        }
    }
}

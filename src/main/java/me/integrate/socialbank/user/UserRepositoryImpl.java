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
public class UserRepositoryImpl implements UserRepository {
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
    private final static String IMAGE = "image";

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public UserRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public User getUserByEmail(String email) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM " + USER_TABLE + " WHERE " + EMAIL + "= ?", new
                    Object[]{email}, new UserRowMapper());
        } catch (EmptyResultDataAccessException ex) {
            throw new UserNotFoundException();
        }
    }

    public User saveUser(User user) {
        try {
            jdbcTemplate.update("INSERT INTO " + USER_TABLE + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                    user.getEmail(), user.getName(), user.getSurname(), user.getPassword(), user.getBirthdate(),
                    user.getGender().toString(), user.getBalance(), user.getDescription(), null, user.getImage());
        } catch (DuplicateKeyException ex) {
            throw new EmailAlreadyExistsException();
        }

        return user;
    }

    public void updatePassword(String email, String password) {
        jdbcTemplate.update("UPDATE " + USER_TABLE + " SET " + PASSWORD + " = ? WHERE " + EMAIL + " = ?", password,
                email);
    }

    public void updateUser(String email, User user) {
        jdbcTemplate.update("UPDATE " + USER_TABLE +
                        " SET " + NAME + " = ?," +
                        SURNAME + " = ?, " +
                        BIRTHDATE + " = ?, " +
                        GENDER + " = ?, " +
                        DESCRIPTION + " = ?, " +
                        IMAGE + " = ? WHERE " + EMAIL + " = ?",
                user.getName(), user.getSurname(), user.getBirthdate(), user.getGender().toString(),
                user.getDescription(), user.getImage(), user.getEmail());
    }

    public void updateRecoveryToken(String email, String recoveryToken) {
        jdbcTemplate.update("UPDATE " + USER_TABLE + " SET " + RECOVERY + " = ? WHERE " + EMAIL + " = ?",
                recoveryToken, email);
    }

    public String getEmailFromToken(String token) {
        String email;
        try {
            email = jdbcTemplate.queryForObject("SELECT email FROM " + USER_TABLE + " WHERE " + RECOVERY + "= ?", new
                    Object[]{token}, String.class);
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
            user.setImage(resultSet.getString(IMAGE));

            String image = "iVBORw0KGgoAAAANSUhEUgAAAWwAAACLCAMAAAByd1MFAAAAflBMVEVra2v///++ADltbW1nZ2f8/PyPj4/rz9D7//9cXFz5" +
                    "+//y//+qCDHCADjGACO3BTrKEzv09PS4ADd3d3e/v7" +
                    "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";
            user.setImage(image);
            return user;
        }
    }
}

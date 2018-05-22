package me.integrate.socialbank.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final static String USER_TABLE = "\"user\"";
    private final static String REQUEST_ACCOUNT_VERIFICATION_TABLE = "request_account_verification";
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
    private final static String VERIFIED = "verified_account";

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
        StringBuilder sql = new StringBuilder("UPDATE " + USER_TABLE + " SET");
        Map<String, Object> fields = new HashMap<>();

        if (user.getName() != null)
            fields.put(NAME, user.getName());
        if (user.getSurname() != null)
            fields.put(SURNAME, user.getSurname());
        if (user.getBirthdate() != null)
            fields.put(BIRTHDATE, user.getBirthdate());
        if (user.getGender() != null)
            fields.put(GENDER, user.getGender().toString());
        if (user.getDescription() != null)
            fields.put(DESCRIPTION, user.getDescription());
        if (user.getImage() != null)
            fields.put(IMAGE, user.getImage());

        for (Iterator<String> it = fields.keySet().iterator(); it.hasNext();) {
            sql.append(" ").append(it.next()).append(" = ?");

            if (it.hasNext()) {
                sql.append(",");
            }
        }
        sql.append(" WHERE email = \'").append(email).append("\'");

        jdbcTemplate.update(sql.toString(), fields.values().toArray());
    }

    public Set<User> getUsers() {
        return new HashSet<>(jdbcTemplate.query("SELECT * FROM " + USER_TABLE, new UserRowMapper()));
    }

    public void saveRequestAccountVerification(String email, String message) {
        try {
            jdbcTemplate.update("INSERT INTO " + REQUEST_ACCOUNT_VERIFICATION_TABLE + " VALUES (?, ?)", email, message);
        } catch (DuplicateKeyException e) {
            throw new PendingAccountVerification();
        }
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
            user.setVerified(resultSet.getBoolean(VERIFIED));

            return user;
        }
    }
}

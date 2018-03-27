package me.integrate.socialbank.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class UserRepository
{
    private final String USER_TABLE = "\"user\"";
    private final String EMAIL = "email";
    private final String NAME = "name";
    private final String SURNAME = "surname";
    private final String PASSWORD = "password";
    private final String BIRTHDATE = "birthdate";
    private final String GENDER = "gender";
    private final String BALANCE = "balance";
    private final String DESCRIPTION = "description";

    private JdbcTemplate jdbcTemplate;

    private class UserRowMapper implements RowMapper<User>
    {

        @Override
        public User mapRow(ResultSet resultSet, int i) throws SQLException
        {
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


    @Autowired
    public UserRepository(JdbcTemplate jdbcTemplate)
    {
        this.jdbcTemplate = jdbcTemplate;
    }

    public User getUserByEmail(String email)
    {
        return jdbcTemplate.queryForObject("SELECT * FROM " + USER_TABLE + " WHERE " + EMAIL + "= ?",
                new Object[]{email}, new UserRowMapper());
    }

    public User saveUser(User user)
    {
        try
        {
            jdbcTemplate.update("INSERT INTO " + USER_TABLE + " VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                    user.getEmail(), user.getName(), user.getSurname(), user.getPassword(), user.getBirthdate(),
                    user.getGender().toString(), user.getBalance(), user.getDescription());
        }
        catch (DuplicateKeyException ex)
        {
            throw new EmailAlreadyExistsException();
        }

        return user;
    }
}

package me.integrate.socialbank.enrollment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ExchangeTokenRepositoryImpl implements ExchangeTokenRepository {

    private final static String EXCHANGE_TOKEN_TABLE = "event_user_token";
    private final static String USER = "\"user\"";
    private final static String EVENT = "event";
    private final static String TOKEN = "token";

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ExchangeTokenRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public String getExchangeToken(int eventId, String username) {
        try {
            String sql = "SELECT " + TOKEN + " FROM " + EXCHANGE_TOKEN_TABLE +
                    " WHERE " + EVENT + " = ? AND " + USER + " = ?";
            return jdbcTemplate.queryForObject(sql, new Object[]{eventId, username}, String.class);
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    @Override
    public void save(int eventId, String username, String exchangeToken) {
        try {
            jdbcTemplate.update("INSERT INTO " + EXCHANGE_TOKEN_TABLE + " VALUES (?, ?, ?)",
                    username, eventId, exchangeToken);
        } catch (DuplicateKeyException ex) {
            throw new TokenAlreadyExistsException();
        }
    }
}

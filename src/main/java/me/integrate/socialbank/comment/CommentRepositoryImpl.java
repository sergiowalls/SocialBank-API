package me.integrate.socialbank.comment;

import me.integrate.socialbank.comment.exception.CommentNotFoundException;
import me.integrate.socialbank.comment.exception.ReferenceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CommentRepositoryImpl implements CommentRepository {

    private static String EVENT_ID = "event_id";
    private static String COMMENT_TABLE = "comment";
    private static String ID = "id";
    private static String CREATOR = "creator_email";
    private static String CREATED_AT = "created_at";
    private static String UPDATED_AT = "updated_at";
    private static String ANSWER_TO = "answer_to";
    private static String CONTENT = "content";

    private JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    @Autowired
    public CommentRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(this.jdbcTemplate).withTableName(COMMENT_TABLE).usingColumns
                (EVENT_ID, CREATOR, CREATED_AT, UPDATED_AT, ANSWER_TO, CONTENT).usingGeneratedKeyColumns(ID);
    }

    @Override
    public Comment getCommentById(int id) {
        Comment comment;
        try {
            final String sql = "SELECT * FROM " + COMMENT_TABLE + " WHERE " + ID + "= ?";
            comment = jdbcTemplate.queryForObject(sql, new Object[]{id}, new CommentRowMapper());
        } catch (EmptyResultDataAccessException e) {
            throw new CommentNotFoundException();
        }
        return comment;
    }

    @Override
    public void updateContent(int id, String content) {
        String sql = "UPDATE " + COMMENT_TABLE + " SET " + CONTENT + " = ?, " + UPDATED_AT + " = ? WHERE " + ID + " =" +
                " ?";
        jdbcTemplate.update(sql, content, new Date(), id);
    }

    @Override
    public Comment saveComment(Comment comment) {
        Map<String, Object> params = new HashMap<>();
        params.put(EVENT_ID, comment.getEventId());
        params.put(CREATOR, comment.getCreatorEmail());
        params.put(CREATED_AT, comment.getCreatedAt());
        params.put(UPDATED_AT, comment.getUpdatedAt());
        params.put(ANSWER_TO, comment.getAnswerTo());
        params.put(CONTENT, comment.getContent());

        try {
            Number id = simpleJdbcInsert.executeAndReturnKey(params);
            comment.setId(id.intValue());
        } catch (DataIntegrityViolationException e) {
            throw new ReferenceNotFoundException();
        }

        return comment;
    }

    @Override
    public List<Comment> getAllComments(int event_id) {
        String sql = "SELECT * FROM " + COMMENT_TABLE + " WHERE " + EVENT_ID + "=?";
        return jdbcTemplate.query(sql, new Object[]{event_id}, new CommentRowMapper());
    }

    @Override
    public void deleteComment(int id) {
        jdbcTemplate.update("DELETE FROM " + COMMENT_TABLE + " WHERE " + ID + "=?", id);
    }

    private class CommentRowMapper implements RowMapper<Comment> {
        @Override
        public Comment mapRow(ResultSet resultSet, int i) throws SQLException {
            Comment comment = new Comment();
            comment.setId(resultSet.getInt(ID));
            comment.setEventId(resultSet.getInt(EVENT_ID));
            comment.setCreatorEmail(resultSet.getString(CREATOR));
            comment.setCreatedAt(resultSet.getTimestamp(CREATED_AT));
            comment.setUpdatedAt(resultSet.getTimestamp(UPDATED_AT));
            Integer answerTo = resultSet.getInt(ANSWER_TO);
            if(!resultSet.wasNull()) comment.setAnswerTo(answerTo);
            comment.setContent(resultSet.getString(CONTENT));
            return comment;
        }
    }


}
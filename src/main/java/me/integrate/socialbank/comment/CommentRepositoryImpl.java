package me.integrate.socialbank.comment;

import me.integrate.socialbank.comment.exception.CommentNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
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

    private static String COMMENT_TABLE = "comments_";
    private static String ID = "id";
    private static String CREATOR = "creator_email";
    private static String CREATED_AT = "created_at";
    private static String UPDATED_AT = "updated_at";
    private static String ANSWER_TO = "answer_to";
    private static String CONTENT = "content";

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public CommentRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Comment getCommentById(int event_id, int id) {
        Comment comment;
        try {
            final String sql = "SELECT * FROM " + COMMENT_TABLE + event_id + " WHERE " + ID + "= ?";
            comment = jdbcTemplate.queryForObject(sql, new Object[]{id}, new CommentRowMapper());
        } catch (EmptyResultDataAccessException e) {
            throw new CommentNotFoundException();
        }
        comment.setEventId(event_id);
        return comment;
    }

    @Override
    public void updateContent(int event_id, int id, String content) {
        final String COMMENTS_TABLE = "comments_"+event_id;
        String sql = "UPDATE " + COMMENTS_TABLE + " SET " + CONTENT + " = ?, " + UPDATED_AT + " = ? WHERE " + ID + " = ?";
        jdbcTemplate.update(sql, content, new Date(), id);
    }

    @Override
    public Comment saveComment(Comment comment) {
        final String COMMENTS_TABLE = "comments_"+ comment.getEventId();
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(this.jdbcTemplate)
                .withTableName(COMMENTS_TABLE)
                .usingColumns(CREATOR, CREATED_AT, UPDATED_AT, ANSWER_TO, CONTENT)
                .usingGeneratedKeyColumns(ID);

        Map<String, Object> params = new HashMap<>();
        params.put(CREATOR, comment.getCreatorEmail());
        params.put(CREATED_AT, comment.getCreatedAt());
        params.put(UPDATED_AT, comment.getUpdatedAt());
        params.put(ANSWER_TO, comment.getAnswerTo());
        params.put(CONTENT, comment.getContent());

        Number id = simpleJdbcInsert.executeAndReturnKey(params);
        comment.setId(id.intValue());

        return comment;
    }

    @Override
    public List<Comment> getAllComments(int event_id) {
        final String COMMENTS_TABLE = "comments_"+event_id;
        return jdbcTemplate.query("SELECT * FROM " + COMMENTS_TABLE, new CommentRowMapper());
    }

    @Override
    public void deleteComment(int event_id, int id) {
        final String COMMENTS_TABLE = "comments_"+event_id;
        jdbcTemplate.update("DELETE FROM " + COMMENTS_TABLE + " WHERE " + ID + "=?", id);
    }

    private class CommentRowMapper implements RowMapper<Comment> {
        @Override
        public Comment mapRow(ResultSet resultSet, int i) throws SQLException {
            Comment comment = new Comment();
            comment.setId(resultSet.getInt(ID));
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
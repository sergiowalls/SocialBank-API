package me.integrate.socialbank.post;

import me.integrate.socialbank.post.exception.PostNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class PostRepositoryImpl implements PostRepository {

    private static String POST_TABLE = "posts_";
    private static String ID = "id";
    private static String CREATOR = "creator_email";
    private static String CREATED_AT = "created_at";
    private static String UPDATED_AT = "updated_at";
    private static String ANSWER_TO = "answer_to";
    private static String CONTENT = "content";

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public PostRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Post getPostById(int event_id, int id) {
        Post post;
        try {
            post = jdbcTemplate.queryForObject("SELECT * FROM " + POST_TABLE + event_id + " WHERE " + ID + "= ?",
                    new Object[]{id}, new PostRowMapper());
        } catch (EmptyResultDataAccessException e) {
            throw new PostNotFoundException();
        }
        post.setEventId(event_id);
        return post;
    }

    @Override
    public Post updateContent(int event_id, int id, String content) {
        return null;
    }

    @Override
    public Post savePost(Post post) {
        final String POSTS_TABLE = "posts_"+post.getEventId();
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(this.jdbcTemplate)
                .withTableName(POSTS_TABLE)
                .usingColumns(CREATOR, CREATED_AT, UPDATED_AT, ANSWER_TO, CONTENT)
                .usingGeneratedKeyColumns(ID);

        Map<String, Object> params = new HashMap<>();
        params.put(CREATOR, post.getCreatorEmail());
        params.put(CREATED_AT, post.getCreatedAt());
        params.put(UPDATED_AT, post.getUpdatedAt());
        params.put(ANSWER_TO, post.getAnswerTo());
        params.put(CONTENT, post.getContent());

        Number id = simpleJdbcInsert.executeAndReturnKey(params);
        post.setId(id.intValue());

        return post;
    }

    @Override
    public List<Post> getAllPosts(int event_id) {
        return null;
    }

    @Override
    public void deletePost(int event_id, int id) {

    }

    private class PostRowMapper implements RowMapper<Post> {
        @Override
        public Post mapRow(ResultSet resultSet, int i) throws SQLException {
            Post post = new Post();
            post.setId(resultSet.getInt(ID));
            post.setCreatorEmail(resultSet.getString(CREATOR));
            post.setCreatedAt(resultSet.getTimestamp(CREATED_AT));
            post.setUpdatedAt(resultSet.getTimestamp(UPDATED_AT));
            Integer answerTo = resultSet.getInt(ANSWER_TO);
            if(!resultSet.wasNull()) post.setAnswerTo(answerTo);
            post.setContent(resultSet.getString(CONTENT));
            return post;
        }
    }


}
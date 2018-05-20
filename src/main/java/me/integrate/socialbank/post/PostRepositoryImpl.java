package me.integrate.socialbank.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class PostRepositoryImpl implements PostRepository {

    private static String EVENT_TABLE = "event";
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
        return null;
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
}

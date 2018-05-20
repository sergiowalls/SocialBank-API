package me.integrate.socialbank.post;

import java.util.List;

public interface PostRepository {
    Post getPostById(int event_id, int id);

    void updateContent(int event_id, int id, String content);

    Post savePost(Post post);

    List<Post> getAllPosts(int event_id);

    void deletePost(int event_id, int id);
}

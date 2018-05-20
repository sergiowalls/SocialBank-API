package me.integrate.socialbank.post;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PostRepositoryImp implements PostRepository {
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
        return null;
    }

    @Override
    public List<Post> getAllPosts(int event_id) {
        return null;
    }

    @Override
    public void deletePost(int event_id, int id) {

    }
}

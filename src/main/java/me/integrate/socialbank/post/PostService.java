package me.integrate.socialbank.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {
    private PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Post getPostById(int event_id, int id) {
        return postRepository.getPostById(event_id, id);
    }

    public Post updatePost(int event_id, int id, String content) {
        return postRepository.updateContent(event_id, id, content);
    }

    public Post savePost(Post post) {
        return postRepository.savePost(post);
    }

    public List<Post> getAllPosts(int event_id) {
        return postRepository.getAllPosts(event_id);
    }

    public void deletePost(int event_id, int id) {
        postRepository.deletePost(event_id, id);
    }
}

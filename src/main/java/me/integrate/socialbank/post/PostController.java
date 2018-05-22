package me.integrate.socialbank.post;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class PostController {
    private PostService postService;

    @Autowired
    public PostController(PostService postService) { this.postService = postService; }

    @GetMapping("/events/{event_id}/posts/{id}")
    public Post getPostById(@PathVariable int event_id, @PathVariable int id) {
        return postService.getPostById(event_id, id);
    }

    @PutMapping("/events/{event_id}/posts/{id}")
    public void updatePostContent(@PathVariable int event_id, @PathVariable int id, @RequestParam String content) {
        postService.updatePost(event_id, id, content);
    }

    @PostMapping("/events/{event_id}/posts")
    @ResponseStatus(HttpStatus.CREATED)
    public Post savePost(@RequestBody Post post, @PathVariable int event_id, Authentication authentication) {
        post.setCreatorEmail(authentication.getName());
        post.setEventId(event_id);
        return postService.savePost(post);
    }

    @GetMapping("/events/{event_id}/posts")
    public @ResponseBody
    List<Post> getAllPosts(@PathVariable int event_id) {
        return postService.getAllPosts(event_id);
    }


    @DeleteMapping("/events/{event_id}/posts/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deletePost(@PathVariable int event_id, @PathVariable int id) {
        postService.deletePost(event_id, id);
    }

}

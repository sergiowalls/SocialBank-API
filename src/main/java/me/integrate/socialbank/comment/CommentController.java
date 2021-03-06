package me.integrate.socialbank.comment;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class CommentController {
    private CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) { this.commentService = commentService; }

    @GetMapping("/comments/{id}")
    public Comment getCommentById(@PathVariable int id) {
        return commentService.getCommentById(id);
    }

    @PutMapping("/comments/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Comment updateCommentContent(@PathVariable int id, @RequestParam String content) {
        return commentService.updateComment(id, content);
    }

    @PostMapping("/events/{event_id}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public Comment saveComment(@RequestBody Comment content, @PathVariable int event_id, Authentication
            authentication) {
        String email = authentication.getName();
        return commentService.saveComment(event_id, email, content.getContent(), null);
    }

    @GetMapping("/events/{event_id}/comments")
    public @ResponseBody
    List<Comment> getAllComments(@PathVariable int event_id) {
        return commentService.getAllComments(event_id);
    }


    @DeleteMapping("/comments/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable int id) {
        commentService.deleteComment(id);
    }

}

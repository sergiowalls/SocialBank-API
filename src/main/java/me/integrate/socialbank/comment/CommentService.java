package me.integrate.socialbank.comment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    private CommentRepository commentRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Comment getCommentById(int id) {
        return commentRepository.getCommentById(id);
    }

    public void updateComment(int id, String content) {
        commentRepository.updateContent(id, content);
    }

    public Comment saveComment(Comment comment) {
        return commentRepository.saveComment(comment);
    }

    public List<Comment> getAllComments(int event_id) {
        return commentRepository.getAllComments(event_id);
    }

    public void deleteComment(int event_id, int id) {
        commentRepository.deleteComment(id);
    }
}

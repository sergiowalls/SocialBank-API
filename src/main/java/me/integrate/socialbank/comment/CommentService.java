package me.integrate.socialbank.comment;

import me.integrate.socialbank.comment.exception.InvalidReferenceException;
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

    public Comment updateComment(int id, String content) {
        commentRepository.updateContent(id, content);
        return commentRepository.getCommentById(id);
    }

    public Comment saveComment(Comment comment) {
        Integer repliedId = comment.getReplyTo();
        if (repliedId != null && commentRepository.getCommentById(repliedId).getEventId() != comment.getEventId())
            throw new InvalidReferenceException();
        return commentRepository.saveComment(comment);
    }

    public List<Comment> getAllComments(int event_id) {
        return commentRepository.getAllComments(event_id);
    }

    public void deleteComment(int id) {
        commentRepository.deleteComment(id);
    }
}

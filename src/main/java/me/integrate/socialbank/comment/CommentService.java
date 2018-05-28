package me.integrate.socialbank.comment;

import me.integrate.socialbank.comment.exception.InvalidReferenceException;
import me.integrate.socialbank.user.User;
import me.integrate.socialbank.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    private CommentRepository commentRepository;

    private UserService userService;

    private void setUserInfo(Comment comment) {
        User creator = userService.getUserByEmail(comment.getCreatorEmail());
        comment.setUserName(creator.getName());
        comment.setUserSurname(creator.getSurname());
    }

    @Autowired
    public CommentService(CommentRepository commentRepository, UserService userService) {
        this.commentRepository = commentRepository;
        this.userService = userService;
    }

    public Comment getCommentById(int id) {

        Comment comment = commentRepository.getCommentById(id);
        setUserInfo(comment);
        return comment;
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
        List<Comment> comments = commentRepository.getAllComments(event_id);
        for (Comment comment : comments) {
            setUserInfo(comment);
        }
        return comments;
    }

    public void deleteComment(int id) {
        commentRepository.deleteComment(id);
    }
}

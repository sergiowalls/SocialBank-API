package me.integrate.socialbank.comment;

import me.integrate.socialbank.comment.exception.InvalidReferenceException;
import me.integrate.socialbank.event.EventService;
import me.integrate.socialbank.user.User;
import me.integrate.socialbank.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CommentService {
    private CommentRepository commentRepository;

    private UserService userService;

    private EventService eventService;

    private void setUserInfo(Comment comment) {
        User creator = userService.getUserByEmail(comment.getCreatorEmail());
        comment.setUserName(creator.getName());
        comment.setUserSurname(creator.getSurname());
    }

    private Comment createStandardComment(String email, int eventId, String content, Integer replyto) {
        Comment comment = new Comment();
        comment.setEventId(eventId);
        comment.setCreatorEmail(email);
        comment.setContent(content);
        comment.setReplyTo(replyto);
        Date date = new Date();
        comment.setCreatedAt(date);
        comment.setUpdatedAt(date);
        return comment;
    }

    @Autowired
    public CommentService(CommentRepository commentRepository, UserService userService, EventService eventService) {
        this.commentRepository = commentRepository;
        this.userService = userService;
        this.eventService = eventService;
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

    public Comment saveComment(int eventId, String email, String content, Integer replyTo) {
        if (replyTo != null && commentRepository.getCommentById(replyTo).getEventId() != eventId)
            throw new InvalidReferenceException();
        Comment comment = createStandardComment(email, eventId, content, replyTo);
        return commentRepository.saveComment(comment);
    }

    public List<Comment> getAllComments(int eventId) {
        List<Comment> comments = commentRepository.getAllComments(eventId);
        for (Comment comment : comments) {
            setUserInfo(comment);
        }
        return comments;
    }

    public void deleteComment(int eventId, int id) {
        //if (eventService.getEventById(eventId).isClosed()) throw new EventAlreadyClosedException(); TODO
        //needs feature/closeEvent to be implemented
        commentRepository.deleteComment(id);
    }
}

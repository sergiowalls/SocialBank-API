package me.integrate.socialbank.comment;

import me.integrate.socialbank.comment.exception.CommentNotFoundException;
import me.integrate.socialbank.comment.exception.InvalidReferenceException;
import me.integrate.socialbank.comment.exception.ReferenceNotFoundException;
import me.integrate.socialbank.event.EventService;
import me.integrate.socialbank.event.EventTestUtils;
import me.integrate.socialbank.user.User;
import me.integrate.socialbank.user.UserService;
import me.integrate.socialbank.user.UserTestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@ExtendWith(SpringExtension.class)
public class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private EventService eventService;

    @Autowired
    private UserService userService;


    private static final String CONTENT = "This is a test content.";

    private Comment createStandardComment(String email, int eventId) {
        Comment comment = new Comment();
        comment.setEventId(eventId);
        comment.setCreatorEmail(email);
        comment.setContent(CONTENT);
        Date date = new Date();
        comment.setCreatedAt(date);
        comment.setUpdatedAt(date);
        return comment;
    }


    @Test
    void givenCommentStoredInDatabaseWhenRetrievedByIdThenReturnsSameComment() {
        String email = "pepito@pepito.com";
        userService.saveUser(UserTestUtils.createUser(email));
        int eventId = eventService.saveEvent(EventTestUtils.createEvent(email)).getId();

        Comment comment = commentService.saveComment(createStandardComment(email, eventId));
        Comment retrievedComment = commentService.getCommentById(comment.getId());
        assertEquals(comment, retrievedComment);
    }

    @Test
    void givenCommentStoredInDatabaseWhenRetrievedByIdThenReturnsCorrectNameAndSurname() {
        String email = "pepito@pepito.com";
        User user = userService.saveUser(UserTestUtils.createUser(email));
        int eventId = eventService.saveEvent(EventTestUtils.createEvent(email)).getId();

        Comment comment = commentService.saveComment(createStandardComment(email, eventId));
        Comment retrievedComment = commentService.getCommentById(comment.getId());
        assertEquals(user.getName(), retrievedComment.getUserName());
        assertEquals(user.getSurname(), retrievedComment.getUserSurname());
    }

    @Test
    void givenCommentStoredInDatabaseWhenDeletedThenIsNoLongerStored() {
        String email = "email@email.tld";
        userService.saveUser(UserTestUtils.createUser(email));
        int eventId = eventService.saveEvent(EventTestUtils.createEvent(email)).getId();

        int id = commentService.saveComment(createStandardComment(email, eventId)).getId();
        commentService.deleteComment(id);

        Assertions.assertThrows(CommentNotFoundException.class, () -> commentService.getCommentById(id));
    }


    @Test
    void givenCommentReferencingCommentNotStoredInDatabaseWhenSavedThenReturnsNotFound() {
        String email = "pepito@pepito.com";
        userService.saveUser(UserTestUtils.createUser(email));
        int eventId = eventService.saveEvent(EventTestUtils.createEvent(email)).getId();

        Comment comment = createStandardComment(email, eventId);
        comment.setReplyTo(123);

        assertThrows(CommentNotFoundException.class, () -> commentService.saveComment(comment));
    }

    @Test
    void givenCommentReferencingCommentFromAnotherEventWhenSavedThenReturnsConflict() {
        String email = "pepito@pepito.com";
        userService.saveUser(UserTestUtils.createUser(email));
        int eventIdOne = eventService.saveEvent(EventTestUtils.createEvent(email)).getId();
        int eventIdTwo = eventService.saveEvent(EventTestUtils.createEvent(email)).getId();

        Comment commentOne = createStandardComment(email, eventIdOne);
        Comment commentTwo = createStandardComment(email, eventIdTwo);

        int commentOneId = commentService.saveComment(commentOne).getId();
        commentTwo.setReplyTo(commentOneId);
        assertThrows(InvalidReferenceException.class, () -> commentService.saveComment(commentTwo));
    }


    @Test
    void givenCommentReferencingEventNotStoredInDatabaseWhenSavedThenReturnsNotFound() {
        String email = "pepito@pepito.com";
        userService.saveUser(UserTestUtils.createUser(email));
        Comment comment = createStandardComment(email, 123);
        assertThrows(ReferenceNotFoundException.class, () -> commentService.saveComment(comment));
    }

    @Test
    void givenCommentStoredInDatabaseWhenEventIsDeletedThenCommentsNoLongerStored() {
        String email = "email@email.tld";
        userService.saveUser(UserTestUtils.createUser(email));
        int eventId = eventService.saveEvent(EventTestUtils.createEvent(email)).getId();

        Comment comment = createStandardComment(email, eventId);
        int id = commentService.saveComment(comment).getId();
        eventService.deleteEvent(eventId);

        Assertions.assertThrows(CommentNotFoundException.class, () -> commentService.getCommentById(id));
    }

    @Test
    void givenCommentStoredInDatabaseWhenContentIsUpdatedThenIsCorrectlyUpdated() {
        String email = "email@email.tld";
        userService.saveUser(UserTestUtils.createUser(email));
        int eventId = eventService.saveEvent(EventTestUtils.createEvent(email)).getId();

        Comment comment = createStandardComment(email, eventId);
        int id = commentService.saveComment(comment).getId();

        String newContent = "newContent";

        assertEquals(newContent, commentService.updateComment(id, newContent).getContent());
    }

    @Test
    void givenCommentsStoredInDatabaseWhenGetAllCommentsOfAnEventThenAllValidCommentsAreReturned() {
        String email = "pepito@pepito.com";
        userService.saveUser(UserTestUtils.createUser(email));
        int eventIdOne = eventService.saveEvent(EventTestUtils.createEvent(email)).getId();
        int eventIdTwo = eventService.saveEvent(EventTestUtils.createEvent(email)).getId();
        Date date = new Date();

        Comment comment = createStandardComment(email, eventIdOne);
        Integer commentOne = commentService.saveComment(comment).getId();

        comment = createStandardComment(email, eventIdOne);
        Integer commentTwo = commentService.saveComment(comment).getId();

        comment = createStandardComment(email, eventIdTwo);
        Integer commentThird = commentService.saveComment(comment).getId();

        Set<Integer> cList = new HashSet<>();
        cList.add(commentOne);
        cList.add(commentTwo);

        List<Comment> rList = commentService.getAllComments(eventIdOne);
        for (Comment r : rList) {
            Integer rId = r.getId();
            assert (cList.contains(rId));
            assert (!rId.equals(commentThird));
        }
    }
    
}

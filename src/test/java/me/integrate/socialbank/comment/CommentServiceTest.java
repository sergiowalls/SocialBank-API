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


    @Test
    void givenCommentStoredInDatabaseWhenRetrievedByIdThenReturnsSameComment() {
        String email = "pepito@pepito.com";
        userService.saveUser(UserTestUtils.createUser(email));
        int eventId = eventService.saveEvent(EventTestUtils.createEvent(email)).getId();

        Comment comment = commentService.saveComment(eventId, email, CONTENT, null);
        Comment retrievedComment = commentService.getCommentById(comment.getId());
        assertEquals(comment, retrievedComment);
    }

    @Test
    void givenCommentStoredInDatabaseWhenRetrievedByIdThenReturnsCorrectNameAndSurname() {
        String email = "pepito@pepito.com";
        User user = userService.saveUser(UserTestUtils.createUser(email));
        int eventId = eventService.saveEvent(EventTestUtils.createEvent(email)).getId();

        Comment comment = commentService.saveComment(eventId, email, CONTENT, null);
        Comment retrievedComment = commentService.getCommentById(comment.getId());
        assertEquals(user.getName(), retrievedComment.getUserName());
        assertEquals(user.getSurname(), retrievedComment.getUserSurname());
    }

    @Test
    void givenCommentStoredInDatabaseWhenDeletedThenIsNoLongerStored() {
        String email = "email@email.tld";
        userService.saveUser(UserTestUtils.createUser(email));
        int eventId = eventService.saveEvent(EventTestUtils.createEvent(email)).getId();

        int id = commentService.saveComment(eventId, email, CONTENT, null).getId();
        commentService.deleteComment(id);

        Assertions.assertThrows(CommentNotFoundException.class, () -> commentService.getCommentById(id));
    }


    @Test
    void givenCommentReferencingCommentNotStoredInDatabaseWhenSavedThenReturnsNotFound() {
        String email = "pepito@pepito.com";
        userService.saveUser(UserTestUtils.createUser(email));
        int eventId = eventService.saveEvent(EventTestUtils.createEvent(email)).getId();

        assertThrows(CommentNotFoundException.class, () -> commentService.saveComment(eventId, email, CONTENT, 123));
    }

    @Test
    void givenCommentReferencingCommentFromAnotherEventWhenSavedThenReturnsConflict() {
        String email = "pepito@pepito.com";
        userService.saveUser(UserTestUtils.createUser(email));
        int eventIdOne = eventService.saveEvent(EventTestUtils.createEvent(email)).getId();
        int eventIdTwo = eventService.saveEvent(EventTestUtils.createEvent(email)).getId();

        int commentOneId = commentService.saveComment(eventIdOne, email, CONTENT, null).getId();
        assertThrows(InvalidReferenceException.class, () -> commentService.saveComment(eventIdTwo, email, CONTENT,
                commentOneId));
    }


    @Test
    void givenCommentReferencingEventNotStoredInDatabaseWhenSavedThenReturnsNotFound() {
        String email = "pepito@pepito.com";
        userService.saveUser(UserTestUtils.createUser(email));
        assertThrows(ReferenceNotFoundException.class, () -> commentService.saveComment(99999, email, CONTENT, null));
    }

    @Test
    void givenCommentStoredInDatabaseWhenEventIsDeletedThenCommentsNoLongerStored() {
        String email = "email@email.tld";
        userService.saveUser(UserTestUtils.createUser(email));
        int eventId = eventService.saveEvent(EventTestUtils.createEvent(email)).getId();

        int id = commentService.saveComment(eventId, email, CONTENT, null).getId();
        eventService.deleteEvent(eventId);

        Assertions.assertThrows(CommentNotFoundException.class, () -> commentService.getCommentById(id));
    }

    @Test
    void givenCommentStoredInDatabaseWhenContentIsUpdatedThenIsCorrectlyUpdated() {
        String email = "email@email.tld";
        userService.saveUser(UserTestUtils.createUser(email));
        int eventId = eventService.saveEvent(EventTestUtils.createEvent(email)).getId();

        int id = commentService.saveComment(eventId, email, CONTENT, null).getId();

        String newContent = "newContent";

        assertEquals(newContent, commentService.updateComment(id, newContent).getContent());
    }

    @Test
    void givenCommentsStoredInDatabaseWhenGetAllCommentsOfAnEventThenAllValidCommentsAreReturned() {
        String email = "pepito@pepito.com";
        userService.saveUser(UserTestUtils.createUser(email));
        int eventIdOne = eventService.saveEvent(EventTestUtils.createEvent(email)).getId();
        int eventIdTwo = eventService.saveEvent(EventTestUtils.createEvent(email)).getId();

        Integer commentOne = commentService.saveComment(eventIdOne, email, CONTENT, null).getId();

        Integer commentTwo = commentService.saveComment(eventIdOne, email, CONTENT, null).getId();

        Integer commentThird = commentService.saveComment(eventIdTwo, email, CONTENT, null).getId();

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

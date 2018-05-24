package me.integrate.socialbank.comment;

import me.integrate.socialbank.comment.exception.CommentNotFoundException;
import me.integrate.socialbank.comment.exception.ReferenceNotFoundException;
import me.integrate.socialbank.event.EventRepositoryImpl;
import me.integrate.socialbank.event.EventTestUtils;
import me.integrate.socialbank.user.UserRepository;
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
class CommentRepositoryTest {
    @Autowired
    private CommentRepositoryImpl commentRepository;

    @Autowired
    private EventRepositoryImpl eventRepository;

    @Autowired
    private UserRepository userRepository;

    private static final String CONTENT = "This is a test content.";


    @Test
    void givenCommentStoredInDatabaseWhenRetrievedByIdThenReturnsSameComment() {
        String email = "pepito@pepito.com";
        userRepository.saveUser(UserTestUtils.createUser(email));
        int eventId = eventRepository.saveEvent(EventTestUtils.createEvent(email)).getId();
        Comment comment = new Comment();
        comment.setEventId(eventId);
        comment.setCreatorEmail(email);
        comment.setContent(CONTENT);
        Date date = new Date();
        comment.setCreatedAt(date);
        comment.setUpdatedAt(date);
        comment = commentRepository.saveComment(comment);
        Comment retrievedComment = commentRepository.getCommentById(comment.getId());
        assertEquals(comment, retrievedComment);
    }

    @Test
    void givenCommentStoredInDatabaseWhenDeletedThenIsNoLongerStored() {
        String email = "email@email.tld";
        userRepository.saveUser(UserTestUtils.createUser(email));
        int eventId = eventRepository.saveEvent(EventTestUtils.createEvent(email)).getId();

        Comment comment = new Comment();
        comment.setEventId(eventId);
        comment.setCreatorEmail(email);
        comment.setContent(CONTENT);
        Date date = new Date();
        comment.setCreatedAt(date);
        comment.setUpdatedAt(date);
        int id = commentRepository.saveComment(comment).getId();
        commentRepository.deleteComment(id);

        Assertions.assertThrows(CommentNotFoundException.class, () -> commentRepository.getCommentById(id));
    }


    @Test
    void givenCommentReferencingCommentNotStoredInDatabaseWhenSavedThenReturnsNotFound() {
        String email = "pepito@pepito.com";
        userRepository.saveUser(UserTestUtils.createUser(email));
        int eventId = eventRepository.saveEvent(EventTestUtils.createEvent(email)).getId();
        Comment comment = new Comment();
        comment.setEventId(eventId);
        comment.setCreatorEmail(email);
        comment.setContent(CONTENT);
        Date date = new Date();
        comment.setCreatedAt(date);
        comment.setUpdatedAt(date);

        comment.setReplyTo(123);

        assertThrows(ReferenceNotFoundException.class, () -> commentRepository.saveComment(comment));
    }


    @Test
    void givenCommentReferencingEventNotStoredInDatabaseWhenSavedThenReturnsNotFound() {
        String email = "pepito@pepito.com";
        userRepository.saveUser(UserTestUtils.createUser(email));
        Comment comment = new Comment();
        comment.setEventId(123);
        comment.setCreatorEmail(email);
        comment.setContent(CONTENT);
        Date date = new Date();
        comment.setCreatedAt(date);
        comment.setUpdatedAt(date);
        assertThrows(ReferenceNotFoundException.class, () -> commentRepository.saveComment(comment));
    }

    @Test
    void givenCommentStoredInDatabaseWhenEventIsDeletedThenCommentsNoLongerStored() {
        String email = "email@email.tld";
        userRepository.saveUser(UserTestUtils.createUser(email));
        int eventId = eventRepository.saveEvent(EventTestUtils.createEvent(email)).getId();

        Comment comment = new Comment();
        comment.setEventId(eventId);
        comment.setCreatorEmail(email);
        comment.setContent(CONTENT);
        Date date = new Date();
        comment.setCreatedAt(date);
        comment.setUpdatedAt(date);
        int id = commentRepository.saveComment(comment).getId();
        eventRepository.deleteEvent(eventId);

        Assertions.assertThrows(CommentNotFoundException.class, () -> commentRepository.getCommentById(id));
    }

    @Test
    void givenCommentStoredInDatabaseWhenContentIsUpdatedThenIsCorrectlyUpdated() {
        String email = "email@email.tld";
        userRepository.saveUser(UserTestUtils.createUser(email));
        int eventId = eventRepository.saveEvent(EventTestUtils.createEvent(email)).getId();

        Comment comment = new Comment();
        comment.setEventId(eventId);
        comment.setCreatorEmail(email);
        comment.setContent(CONTENT);
        Date date = new Date();
        comment.setCreatedAt(date);
        comment.setUpdatedAt(date);
        int id = commentRepository.saveComment(comment).getId();

        String newContent = "newContent";
        commentRepository.updateContent(id, newContent);
        assertEquals(newContent, commentRepository.getCommentById(id).getContent());
    }

    @Test
    void givenCommentsStoredInDatabaseWhenGetAllCommentsOfAnEventThenAllValidCommentsAreReturned() {
        String email = "pepito@pepito.com";
        userRepository.saveUser(UserTestUtils.createUser(email));
        int eventIdOne = eventRepository.saveEvent(EventTestUtils.createEvent(email)).getId();
        int eventIdTwo = eventRepository.saveEvent(EventTestUtils.createEvent(email)).getId();
        Date date = new Date();

        Comment comment = new Comment();
        comment.setCreatorEmail(email);
        comment.setCreatedAt(date);
        comment.setUpdatedAt(date);
        comment.setContent(CONTENT);

        comment.setEventId(eventIdOne);
        Integer commentOne = commentRepository.saveComment(comment).getId();

        comment = new Comment();
        comment.setCreatorEmail(email);
        comment.setCreatedAt(date);
        comment.setUpdatedAt(date);
        comment.setContent(CONTENT);
        comment.setEventId(eventIdOne);
        Integer commentTwo = commentRepository.saveComment(comment).getId();

        comment = new Comment();
        comment.setCreatorEmail(email);
        comment.setCreatedAt(date);
        comment.setUpdatedAt(date);
        comment.setContent(CONTENT);

        comment.setEventId(eventIdTwo);
        Integer commentThird = commentRepository.saveComment(comment).getId();

        Set<Integer> cList = new HashSet<>();
        cList.add(commentOne);
        cList.add(commentTwo);

        List<Comment> rList = commentRepository.getAllComments(eventIdOne);
        for (Comment r : rList) {
            Integer rId = r.getId();
            assert (cList.contains(rId));
            assert (!rId.equals(commentThird));
        }
    }

}


package me.integrate.socialbank.post;

import me.integrate.socialbank.event.EventRepositoryImpl;
import me.integrate.socialbank.event.EventTestUtils;
import me.integrate.socialbank.post.exception.PostNotFoundException;
import me.integrate.socialbank.user.UserRepository;
import me.integrate.socialbank.user.UserTestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@ExtendWith(SpringExtension.class)
class PostRepositoryTest {
    @Autowired
    private PostRepositoryImpl postRepository;

    @Autowired
    private EventRepositoryImpl eventRepository;

    @Autowired
    private UserRepository userRepository;

    private static final String CONTENT = "This is a test content.";

    @Test
    void givenPostStoredInDatabaseWhenRetrievedByIdThenReturnsSamePost() {
        String email = "pepito@pepito.com";
        userRepository.saveUser(UserTestUtils.createUser(email));
        int eventId = eventRepository.saveEvent(EventTestUtils.createEvent(email)).getId();
        Post post = new Post();
        post.setEventId(eventId);
        post.setCreatorEmail(email);
        post.setContent(CONTENT);
        Date date = new Date();
        post.setCreatedAt(date);
        post.setUpdatedAt(date);
        post = postRepository.savePost(post);
        Post retrievedPost = postRepository.getPostById(eventId, post.getId());
        assertEquals(post, retrievedPost);
    }

    @Test
    void givenPostStoredInDatabaseWhenDeletedThenIsNoLongerStored() {
        String email = "email@email.tld";
        userRepository.saveUser(UserTestUtils.createUser(email));
        int eventId = eventRepository.saveEvent(EventTestUtils.createEvent(email)).getId();

        Post post = new Post();
        post.setEventId(eventId);
        post.setCreatorEmail(email);
        post.setContent(CONTENT);
        Date date = new Date();
        post.setCreatedAt(date);
        post.setUpdatedAt(date);
        int id = postRepository.savePost(post).getId();
        postRepository.deletePost(eventId, id);

        Assertions.assertThrows(PostNotFoundException.class, () -> postRepository.getPostById(eventId, id));
    }

    @Test
    void givenPostStoredInDatabaseWhenEventIsDeletedThenPostsNoLongerStored() {
        String email = "email@email.tld";
        userRepository.saveUser(UserTestUtils.createUser(email));
        int eventId = eventRepository.saveEvent(EventTestUtils.createEvent(email)).getId();

        Post post = new Post();
        post.setEventId(eventId);
        post.setCreatorEmail(email);
        post.setContent(CONTENT);
        Date date = new Date();
        post.setCreatedAt(date);
        post.setUpdatedAt(date);
        int id = postRepository.savePost(post).getId();
        eventRepository.deleteEvent(eventId);

        Assertions.assertThrows(InvalidDataAccessResourceUsageException.class, () -> postRepository.getPostById(eventId, id));
    }

}


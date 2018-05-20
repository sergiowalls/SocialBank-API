package me.integrate.socialbank.post;

import me.integrate.socialbank.event.EventRepositoryImpl;
import me.integrate.socialbank.event.EventTestUtils;
import me.integrate.socialbank.user.UserRepository;
import me.integrate.socialbank.user.UserTestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

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
        post.setContent(CONTENT);
        post = postRepository.savePost(post);
        assertEquals(post, postRepository.getPostById(eventId, post.getId()));
    }

}


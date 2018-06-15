package me.integrate.socialbank.user;

import me.integrate.socialbank.event.Event;
import me.integrate.socialbank.event.EventRepository;
import me.integrate.socialbank.event.EventTestUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class UserIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void whenUserGetsVerifiedAlsoGetsVerifiedAward() {
        User user = UserTestUtils.createUser("swaggaaa@aimassist.me");
        userRepository.saveUser(user);

        assertTrue(true);

        String sql = "UPDATE \"user\" SET verified_account = TRUE WHERE email = 'swaggaaa@aimassist.me'";
        jdbcTemplate.execute(sql);
        assertTrue(userRepository.getUserAwards("swaggaaa@aimassist.me").contains(Award.VERIFIED_USER));
    }

    @Test
    void whenUserOrganizesManyEventsAlsoGetsOrganizerAward() {
        User user = UserTestUtils.createUser("swaggaaa@aimassist.me");
        userRepository.saveUser(user);

        for (int i = 0; i < 5; ++i)
            eventRepository.saveEvent(EventTestUtils.createEvent("swaggaaa@aimassist.me"));

        assertTrue(userRepository.getUserAwards("swaggaaa@aimassist.me").contains(Award.TOP_ORGANIZER));
    }

    @Test
    @Disabled
    void whenUserEnrollsToManyEventsAlsoGetsActiveUserAward() {
        User user = UserTestUtils.createUser("swaggaaa@aimassist.me");
        userRepository.saveUser(user);
        Event event = EventTestUtils.createEvent("swaggaaa@aimassist.me");
        Event event2 = EventTestUtils.createEvent("swaggaaa@aimassist.me");
        Event event3 = EventTestUtils.createEvent("swaggaaa@aimassist.me");
    }
}

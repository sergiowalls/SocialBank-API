package me.integrate.socialbank.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Set;

import static me.integrate.socialbank.user.UserTestUtils.createUser;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ExtendWith(SpringExtension.class)
class UserServiceTest {
    @Autowired
    UserService userService;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Test
    void givenUserWhenSaveItThenReturnsSameUser() {
        String email = "admin@integrate.me";
        String password = "123";
        User user = createUser(email, password);
        User encryptedUser = userService.saveUser(user);

        assertEquals(user, encryptedUser);

    }

    @Test
    void givenNewUserWhenSaveItThenUserIsNotVerified() {
        String email = "admin@integrate.me";
        String password = "123";
        User user = createUser(email, password);
        user.setVerified(true);
        user = userService.saveUser(user);

        assertFalse(user.getVerified());
    }

    @Test
    void givenUserWhenUpdateItsPasswordThenReturnsNewEncryptedPassword() {
        String email = "admin@integrate.me";
        String password = "123";
        String newPassword = "456";
        userService.saveUser(createUser(email, password));
        userService.updatePassword(email, newPassword);
        User encryptedUser = userService.getUserByEmail(email);

        assertTrue(bCryptPasswordEncoder.matches(newPassword, encryptedUser.getPassword()));
    }

    @Test
    void givenUserWhenUpdatedAllInfoGetsUpdated() {
        String email = "admin@integrate.me";
        String password = "123";
        User user = createUser(email, password);
        userService.saveUser(user);

        User newUser = new User();
        newUser.setEmail(email);
        newUser.setPassword(password);
        newUser.setBalance(user.getBalance());

        newUser.setName("newName");
        newUser.setDescription("newDescription");
        newUser.setBirthdate(new Date());
        newUser.setGender(User.Gender.OTHER);
        newUser.setImage("MTIz");
        newUser.setSurname("newSurName");
        userService.updateUser(email, newUser);

        User userByEmail = userService.getUserByEmail(email);
        assertNotEquals(user, userByEmail);
        assertEquals(newUser, userByEmail);
    }

    @Test
    void givenTwoUsersReturnSameUsers() {
        String email = "aaa@aaa.aaa";
        String password = "123";
        String email2 = "bbb@bbb.bbb";

        User user = createUser(email, password);
        userService.saveUser(user);
        User user1 = createUser(email2, password);
        userService.saveUser(user1);

        Set<User> users = userService.getUsers();
        assertTrue(users.contains(user));
        assertTrue(users.contains(user1));
    }

    @Test
    void givenUserWhenRequestAccountVerificationThenUserHasPendingVerification() {
        User user = createUser("aaa@aaa.aaa");
        userService.saveUser(user);

        userService.requestAccountVerification("aaa@aaa.aaa", "Please");

        String sql = "SELECT COUNT(*) FROM request_account_verification WHERE \"user\" = ?";
        int res = jdbcTemplate.queryForObject(sql, Integer.class, "aaa@aaa.aaa");
        assertEquals(1, res);
    }

    @Test
    void givenUserWhenRequestAccountVerificationThenMessageIsSaved() {
        User user = createUser("aaa@aaa.aaa");
        userService.saveUser(user);

        userService.requestAccountVerification("aaa@aaa.aaa", "Please verify me");

        String sql = "SELECT message FROM request_account_verification WHERE \"user\" = ?";
        String message = jdbcTemplate.queryForObject(sql, String.class, "aaa@aaa.aaa");
        assertEquals("Please verify me", message);
    }

    @Test
    void givenUserWithPendingAccountVerificationWhenRequestAccountVerificationThenThrowsException() {
        User user = createUser("aaa@aaa.aaa");
        userService.saveUser(user);

        userService.requestAccountVerification("aaa@aaa.aaa", "Please");

        assertThrows(PendingAccountVerificationException.class,
                () -> userService.requestAccountVerification("aaa@aaa.aaa", "Please"));
    }
}
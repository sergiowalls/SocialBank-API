package me.integrate.socialbank.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static me.integrate.socialbank.user.UserTestUtils.createUser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
@ExtendWith(SpringExtension.class)
class UserServiceTest {
    @Autowired
    UserService userService;

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
    void givenUserWhenUpdateItsPasswordThenReturnsNewEncryptedPassword() {
        String email = "admin@integrate.me";
        String password = "123";
        String newPassword = "456";
        userService.saveUser(createUser(email, password));
        userService.updatePassword(email, newPassword);
        User encryptedUser = userService.getUserByEmail(email);

        assertTrue(bCryptPasswordEncoder.matches(newPassword, encryptedUser.getPassword()));
    }
}
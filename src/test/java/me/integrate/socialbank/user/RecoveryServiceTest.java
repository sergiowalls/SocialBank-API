package me.integrate.socialbank.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ExtendWith(SpringExtension.class)
class RecoveryServiceTest {
    @Autowired
    RecoveryService recoveryService;

    @Autowired
    UserService userService;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Test
    void givenUserWhenRequestEmailGetUUID() {
        User user = UserTestUtils.createUser("ejemplo@integrate.me", "123");
        userService.saveUser(user);

        String UUID = recoveryService.requestEmail("ejemplo@integrate.me");
        assertFalse(UUID.equals(""));
    }

    @Test
    void givenUserWhenRequestChangeThenPasswordUpdates() {
        User user = UserTestUtils.createUser("ejemplo@integrate.me", "123");
        userService.saveUser(user);

        String UUID = recoveryService.requestEmail("ejemplo@integrate.me");
        recoveryService.requestPasswordChange(UUID, "456");

        User encryptedUser = userService.getUserByEmail("ejemplo@integrate.me");
        assertTrue(bCryptPasswordEncoder.matches("456", encryptedUser.getPassword()));
    }

    @Test
    void givenIncorrectTokenWhenRequestChangeThrowsException() {
        User user = UserTestUtils.createUser("ejemplo@integrate.me", "123");
        userService.saveUser(user);

        recoveryService.requestEmail("ejemplo@integrate.me");
        assertThrows(UserNotFoundException.class, () ->
                recoveryService.requestPasswordChange("fake-token-123", "456"));
    }
}

package me.integrate.socialbank.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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

    @MockBean
    MailSender mailSender;

    @Test
    void givenUserWhenRequestChangeThenPasswordUpdates() {
        User user = UserTestUtils.createUser("ejemplo@integrate.me", "123");
        userService.saveUser(user);

        recoveryService.requestEmail("ejemplo@integrate.me");
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(mailSender, times(1)).sendRecoveryEmail(any(), captor.capture());
        recoveryService.requestPasswordChange(captor.getValue(), "456");

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

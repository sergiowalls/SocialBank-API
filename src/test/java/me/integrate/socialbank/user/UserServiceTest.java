package me.integrate.socialbank.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;

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

    private User createDummyUser(String email, String password) {
        User user = new User();
        user.setEmail(email);
        user.setBalance(1337.f);
        try {
            user.setBirthdate(new SimpleDateFormat("yyyy-MM-dd").parse("2017-03-03"));
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        user.setDescription("asd");
        user.setGender(User.Gender.FEMALE);
        user.setName("Manolo");
        user.setSurname("Del Campo");
        user.setPassword(password);
        return user;
    }

    @Test
    void saveUser() {
        String email = "admin@integrate.me";
        String password = "123";
        User user = createDummyUser(email, password);
        User encryptedUser = userService.saveUser(user);

        assertEquals(user, encryptedUser);
    }

    @Test
    void updatePassword() {
        String email = "admin@integrate.me";
        String password = "123";
        String newPassword = "456";
        userService.saveUser(createDummyUser(email, password));
        userService.updatePassword(email, newPassword);
        User encryptedUser = userService.getUserByEmail(email);

        assertTrue(bCryptPasswordEncoder.matches(newPassword, encryptedUser.getPassword()));
    }
}
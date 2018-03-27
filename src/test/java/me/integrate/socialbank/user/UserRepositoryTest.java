package me.integrate.socialbank.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@ExtendWith(SpringExtension.class)
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    private User createDummyUser(String email) {
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
        user.setPassword("JordiJAJAJ");
        return user;
    }

    @Test
    void getUserByEmail() {
        String email = "swaggaaa@integrate.me";
        User user = createDummyUser(email);
        userRepository.saveUser(user);

        assertEquals(user, userRepository.getUserByEmail(email));
    }

    @Test
    void saveUsers() {
        String emailOne = "swaggaaa@integrate.me";
        String emailTwo = "wallz@integrate.me";
        User userOne = createDummyUser(emailOne);
        User userTwo = createDummyUser(emailTwo);
        userRepository.saveUser(userOne);
        userRepository.saveUser(userTwo);

        assertEquals(userOne, userRepository.getUserByEmail(emailOne));
        assertEquals(userTwo, userRepository.getUserByEmail(emailTwo));

    }

    @Test
    public void saveInvalidUser() {
        String email = "swaggaaa@integrate.me";
        User user = createDummyUser(email);
        userRepository.saveUser(user);

        assertThrows(EmailAlreadyExistsException.class, () -> userRepository.saveUser(user)); //Duplicate primary key
    }

    @Test
    public void updatePassword() {
        String email = "swaggaaa@integrate.me";
        String newPassword = "press123forgf";
        User user = createDummyUser(email);
        userRepository.saveUser(user);
        userRepository.updatePassword(email, newPassword);
        assertEquals(newPassword, userRepository.getUserByEmail(email).getPassword());
    }
}
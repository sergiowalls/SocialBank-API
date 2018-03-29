package me.integrate.socialbank.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@ExtendWith(SpringExtension.class)
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    void givenUserStoredInDatabaseWhenRetrievedByEmailThenReturnsSameUser() {
        String email = "swaggaaa@integrate.me";
        User user = UserTestUtils.createUser(email);
        userRepository.saveUser(user);

        assertEquals(user, userRepository.getUserByEmail(email));
    }

    @Test
    void givenTwoDifferentUsersWhenSavedThenReturnSameUsers() {
        String emailOne = "swaggaaa@integrate.me";
        String emailTwo = "wallz@integrate.me";
        User userOne = UserTestUtils.createUser(emailOne);
        User userTwo = UserTestUtils.createUser(emailTwo);
        userRepository.saveUser(userOne);
        userRepository.saveUser(userTwo);

        assertEquals(userOne, userRepository.getUserByEmail(emailOne));
        assertEquals(userTwo, userRepository.getUserByEmail(emailTwo));

    }

    @Test
    public void givenUserWhenSavedTwiceThenThrowsException() {
        String email = "swaggaaa@integrate.me";
        User user = UserTestUtils.createUser(email);
        userRepository.saveUser(user);

        assertThrows(EmailAlreadyExistsException.class, () -> userRepository.saveUser(user)); //Duplicate primary key
    }

    @Test
    public void givenUserWhenUpdatesPasswordThenReturnNewPassword() {
        String email = "swaggaaa@integrate.me";
        String newPassword = "press123forgf";
        User user = UserTestUtils.createUser(email);
        userRepository.saveUser(user);
        userRepository.updatePassword(email, newPassword);
        assertEquals(newPassword, userRepository.getUserByEmail(email).getPassword());
    }
}
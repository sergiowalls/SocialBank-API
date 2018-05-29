package me.integrate.socialbank.user;

import java.util.Set;

public interface UserRepository {
    User getUserByEmail(String email);

    User saveUser(User user);

    void updatePassword(String email, String password);

    void updateRecoveryToken(String email, String recoveryToken);

    String getEmailFromToken(String token);

    void updateUser(String email, User user);

    Set<User> getUsers();

    void reportUser(String reporter, String reported);

    void saveRequestAccountVerification(String email, String message);

    Set<Award> getUserAwards(String email);
}

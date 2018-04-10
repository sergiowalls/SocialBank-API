package me.integrate.socialbank.user;

public interface UserRepository {
    User getUserByEmail(String email);

    User saveUser(User user);

    void updatePassword(String email, String password);

    void updateRecoveryToken(String email, String recoveryToken);

    String getEmailFromToken(String token);

}

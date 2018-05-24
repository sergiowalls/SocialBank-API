package me.integrate.socialbank.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {
    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public User getUserByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }

    public User saveUser(User user) {
        user.setVerified(false);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return userRepository.saveUser(user);
    }

    public void updatePassword(String email, String password) {
        userRepository.updatePassword(email, bCryptPasswordEncoder.encode(password));
    }

    public void updateUser(String email, User user) {
        userRepository.updateUser(email, user);
    }

    public Set<User> getUsers() {
        return userRepository.getUsers();
    }

    public void reportUser(String reporter, String reported) {
        getUserByEmail(reported);
        userRepository.reportUser(reporter, reported);
    }

    public void requestAccountVerification(String email, String message) {
        userRepository.saveRequestAccountVerification(email, message);
    }

    public Set<String> getUserAwards(String email) {
        Set<Award> awards = userRepository.getUserAwards(email);
        Set<String> awardNames = new HashSet<>();
        for (Award award : awards) {
            awardNames.add(award.name());
        }
        return awardNames;
    }
}

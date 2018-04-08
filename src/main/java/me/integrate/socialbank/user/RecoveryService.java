package me.integrate.socialbank.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RecoveryService {
    private UserRepository userRepository;
    private UserService userService;

    @Autowired
    public RecoveryService(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    public String requestEmail(String email) {
        String recoveryToken = UUID.randomUUID().toString();
        userRepository.updateRecoveryToken(email, recoveryToken);

        return recoveryToken;
    }

    public void requestPasswordChange(String token, String newPassword) {
        String email = userRepository.getEmailFromToken(token);
        userService.updatePassword(email, newPassword);
        userRepository.updateRecoveryToken(email, null);
    }
}

package me.integrate.socialbank.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RecoveryService {
    private UserRepository userRepository;
    private UserService userService;
    private MailSender mailSender;

    @Autowired
    public RecoveryService(UserRepository userRepository, UserService userService, MailSender mailSender) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.mailSender = mailSender;
    }

    public void requestEmail(String email) {
        String recoveryToken = UUID.randomUUID().toString();
        userRepository.updateRecoveryToken(email, recoveryToken);

        mailSender.sendRecoveryEmail(email, recoveryToken);
    }

    public void requestPasswordChange(String token, String newPassword) {
        String email = userRepository.getEmailFromToken(token);
        userService.updatePassword(email, newPassword);
        userRepository.updateRecoveryToken(email, null);
    }
}

package me.integrate.socialbank.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RecoveryService
{
    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public RecoveryService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder)
    {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public String requestEmail(String email)
    {
        String recoveryToken = UUID.randomUUID().toString();
        userRepository.updateRecoveryToken(email, recoveryToken);

        return recoveryToken;
    }

    public void requestPasswordChange(String token, String newPassword)
    {
        String email = userRepository.getEmailFromToken(token);
        userRepository.updatePassword(email, bCryptPasswordEncoder.encode(newPassword));
        userRepository.updateRecoveryToken(email, null);
    }
}

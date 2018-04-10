package me.integrate.socialbank.user;

public interface MailSender {
    /**
     * Sends a recovery password email and returns the email sent
     **/
    String sendRecoveryEmail(String recipient, String recoveryToken);
}

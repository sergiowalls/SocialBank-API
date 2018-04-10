package me.integrate.socialbank.user;

public interface IMailSender {
    /**
     * Sends a recovery password email and returns the email sent
     **/
    String sendRecoveryEmail(String recipient, String recoveryToken);
}

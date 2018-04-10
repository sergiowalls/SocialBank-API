package me.integrate.socialbank.user;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

public class GmailSender implements IMailSender {
    private String from;
    private JavaMailSenderImpl mailSender;

    GmailSender(String from) {
        this.from = from;

        mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        //TODO: Move this somewhero elsooo
        mailSender.setUsername(from);
        mailSender.setPassword("joansanchezspring");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");
    }

    @Override
    public String sendRecoveryEmail(String recipient, String recoveryToken) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(recipient);
        msg.setFrom(from);
        msg.setSubject("SocialBank APP - Recovery Password");
        String emailText = "You are receiving this email because you requested a password change. Here's your code: " + recoveryToken;
        msg.setText(emailText);

        try {
            mailSender.send(msg);
        } catch (MailException ex) {
            ex.printStackTrace();
        }

        return emailText;
    }
}

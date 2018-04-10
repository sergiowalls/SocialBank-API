package me.integrate.socialbank.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class GmailSender implements MailSender {

    private final String from;
    private JavaMailSenderImpl mailSender;

    @Autowired
    GmailSender(@Value("${mail.host}") String host, @Value("${mail.port}") int port,
                @Value("${mail.from}") String from, @Value("${mail.password}") String password) {
        this.from = from;

        mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(from);
        mailSender.setPassword(password);

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

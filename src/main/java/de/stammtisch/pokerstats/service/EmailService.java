package de.stammtisch.pokerstats.service;

import java.io.IOException;
import java.nio.charset.Charset;

import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class EmailService {

	private JavaMailSender mailSender;

	@Async
    public void send(String emailAddress, String email, String subject) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(email, true);
            helper.setTo(emailAddress);
            helper.setSubject(subject);
            helper.setFrom("mugglemail420@gmail.com");
            this.mailSender.send(mimeMessage);
        } catch (MessagingException e) {
        }
    }
    
    public void sendConfirmationMail(String username, String emailAddress, String token, long expirationDate) {
    	String mail;
        try {
            mail = StreamUtils.copyToString(new ClassPathResource("mails/ConfirmationMail.html").getInputStream(), Charset.defaultCharset());
        } catch (IOException e) {
            return;
        }
        mail = mail.replace("{USERNAME}", username);
        mail = mail.replace("{LINK}", "http://localhost:8080/api/v1/auth/confirm?confirmation=" + token);
        
        this.send(emailAddress, mail, "Email Bestätigung");
    }
    
    public void sendPasswordResetMail(String username, String emailAddress, String token, long expirationDate) {
    	String mail;
        try {
            mail = StreamUtils.copyToString(new ClassPathResource("mails/PasswordResetMail.html").getInputStream(), Charset.defaultCharset());
        } catch (IOException e) {
            return;
        }
        
        this.send(emailAddress, mail, "Passwort Zurücksetzung");
    }
}

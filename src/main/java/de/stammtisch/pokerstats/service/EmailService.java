package de.stammtisch.pokerstats.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

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
            helper.setFrom("stammtischhub@gmail.com");
            this.mailSender.send(mimeMessage);
        } catch (MessagingException e) {
        }
    }
}

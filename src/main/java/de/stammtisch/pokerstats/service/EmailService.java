package de.stammtisch.pokerstats.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class EmailService {

	@Autowired
	private JavaMailSender mailSender;

    @Async
    public void send(String emailAdresse, String email) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(email, true);
            helper.setTo(emailAdresse);
            helper.setSubject("Confirm your email");
            helper.setFrom("bestbuyzConfirmation@gmail.com");
            this.mailSender.send(mimeMessage);
        } catch (MessagingException e) {
        }
    }
}

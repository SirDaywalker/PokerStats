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
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Service
@RequiredArgsConstructor
public class EmailService {
	
	private final JavaMailSender mailSender;
	
	private String homeUrlPath;
	
	@Async
    public void send(
    		String emailAddress, 
    		String email, 
    		String subject
    ) {
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
    
	@Async
    public void sendConfirmationMail(
    		String username, 
    		String emailAddress, 
    		String token
    ) {
    	String mail;
        try {
            mail = StreamUtils.copyToString(new ClassPathResource("mails/confirmation-mail.html").getInputStream(), Charset.defaultCharset());
        } catch (IOException e) {
            return;
        }
        mail = mail.replace("{USERNAME}", username);
        mail = mail.replace("{LINKHOME}", homeUrlPath);
        mail = mail.replace("{LINK}", homeUrlPath + "confirm-redirect?confirmation=" + token);
        
        this.send(emailAddress, mail, "Email Bestätigung");
    }
    
	@Async
    public void sendPasswordResetMail(
    		String username, 
    		String emailAddress, 
    		String token
    ) {
    	String mail;
        try {
            mail = StreamUtils.copyToString(new ClassPathResource("mails/password-reset-mail.html").getInputStream(), Charset.defaultCharset());
        } catch (IOException e) {
            return;
        }
        mail = mail.replace("{USERNAME}", username);
        mail = mail.replace("{LINKHOME}", homeUrlPath);
        mail = mail.replace("{LINK}", homeUrlPath + "http://localhost:8080/password-reset-form?confirmation=" + token);

        this.send(emailAddress, mail, "Passwort Zurücksetzung");
    }
	
	@Async
    public void sendInvoiceMail(
    		String username, 
    		String emailAddress, 
    		String title,
    		String creditor, 
    		String amount, 
    		String date, 
    		String interest,
    		String interestInterval
    ) {
    	String mail;
        try {
            mail = StreamUtils.copyToString(new ClassPathResource("mails/invoice-mail.html").getInputStream(), Charset.defaultCharset());
        } catch (IOException e) {
            return;
        }
        mail = mail.replace("{LINKHOME}", homeUrlPath);
        mail = mail.replace("{USERNAME}", username);
        mail = mail.replace("{TITLE}", title);
        mail = mail.replace("{CREDITOR}", creditor);
        mail = mail.replace("{AMOUNT}", amount);
        mail = mail.replace("{DATE}", date);
        mail = mail.replace("{INTEREST}", interest);
        mail = mail.replace("{INTERESTINTERVAL}", interestInterval);

        this.send(emailAddress, mail, "Rechnung erhalten");
    }
}

package de.stammtisch.pokerstats.service;

import de.stammtisch.pokerstats.models.Confirmation;
import de.stammtisch.pokerstats.models.User;
import de.stammtisch.pokerstats.repository.ConfirmationRepository;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ConfirmationService {
    private final ConfirmationRepository confirmationRepository;

    public Confirmation createConfirmation(User user) {
        Confirmation confirmation = new Confirmation();
        String token = UUID.randomUUID().toString();
        confirmation.setToken(token);
        confirmation.setValidatedAt(0);
        confirmation.setUser(user);

        return confirmationRepository.save(confirmation);
    }

    public void sendConfirmationMail(String username, String token) {
        String mail;
        try {
            mail = StreamUtils.copyToString(new ClassPathResource("mails/ConfirmationMail.txt").getInputStream(), Charset.defaultCharset());
        } catch (IOException e) {
            return;
        }
        mail = mail.replace("{USERNAME}", username);
        mail = mail.replace("{LINK}", "http://localhost:8080/api/v1/auth/register/confirm?token=" + token);
        String date = LocalDate.now().toString();
        mail = mail.replace("{DATE}", "");
    }
}

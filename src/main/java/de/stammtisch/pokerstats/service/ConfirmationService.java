package de.stammtisch.pokerstats.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import de.stammtisch.pokerstats.models.Confirmation;
import de.stammtisch.pokerstats.models.User;
import de.stammtisch.pokerstats.repository.ConfirmationRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ConfirmationService {
    
	private final ConfirmationRepository confirmationRepository;
    private final EmailService emailService;

    public Confirmation createConfirmation(User user) {
        Confirmation confirmation = new Confirmation();
        String token = UUID.randomUUID().toString();
        confirmation.setToken(token);
        confirmation.setValidatedAt(0);
        confirmation.setUser(user);

        return confirmationRepository.save(confirmation);
    }

    public void sendConfirmationMail(User user, Confirmation confirmation) {
    	//900000ms = 15min (expiration)
        this.emailService.sendConfirmationMail(user.getUsername(), user.getEmail(), confirmation.getToken(), confirmation.getId()+900000);
    }
    
}

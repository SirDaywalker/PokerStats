package de.stammtisch.pokerstats.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import de.stammtisch.pokerstats.exceptions.ConfirmationAlreadyUsedException;
import de.stammtisch.pokerstats.exceptions.ConfirmationTimeExceededException;
import de.stammtisch.pokerstats.exceptions.UserAlreadyEnabledException;
import de.stammtisch.pokerstats.models.Confirmation;
import de.stammtisch.pokerstats.models.User;
import de.stammtisch.pokerstats.repository.ConfirmationRepository;
import de.stammtisch.pokerstats.repository.UserRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ConfirmationService {
    
	private final ConfirmationRepository confirmationRepository;
	private final UserRepository userRepository;
    private final EmailService emailService;

    public Confirmation createConfirmation(User user) {
        Confirmation confirmation = new Confirmation();
        String token = UUID.randomUUID().toString();
        confirmation.setToken(token);
        confirmation.setValidatedAt(0);
        confirmation.setUser(user);

        return this.confirmationRepository.save(confirmation);
    }

    public void sendConfirmationMail(User user, Confirmation confirmation) {
        this.emailService.sendConfirmationMail(user.getUsername(), user.getEmail(), confirmation.getToken());
    }

	public void sendPasswordResetMail(User user, Confirmation confirmation) {
		this.emailService.sendPasswordResetMail(user.getUsername(), user.getEmail(), confirmation.getToken());
	}
    
    public void confirmUser(@NonNull String confirmation) {
		final Confirmation conf = this.confirmationRepository.findByToken(confirmation).orElseThrow();
		final User user = conf.getUser();
		
		long curTime = System.currentTimeMillis();
		//900000ms = 15min
		if(curTime - conf.getId() > 900000) {
			this.confirmationRepository.deleteById(conf.getId());
			throw new ConfirmationTimeExceededException(); 
		}
		if(user.isEnabled()) {
			this.confirmationRepository.deleteById(conf.getId());
			throw new UserAlreadyEnabledException();
		}
		if(conf.getValidatedAt() != 0) {
			this.confirmationRepository.deleteById(conf.getId());
			throw new ConfirmationAlreadyUsedException();
		}
			
		conf.setValidatedAt(curTime);
		this.confirmationRepository.save(conf);
		
		user.setEnabled(true);
		this.userRepository.save(user);

		Set<Confirmation> confs = this.confirmationRepository.findByUser(user).orElse(Set.of());
		for(Confirmation con : confs){
			if(con.getValidatedAt() != 0) { continue; }
			this.confirmationRepository.deleteById(con.getId());
		}
		
    }
    
    public boolean existsByToken(String token) {
    	return this.confirmationRepository.existsByToken(token);
    }
    
    public Confirmation findByToken(String confirmation) {
    	return this.confirmationRepository.findByToken(confirmation).orElseThrow();
    }
    
}

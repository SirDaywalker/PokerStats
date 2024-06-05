package de.stammtisch.pokerstats.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import de.stammtisch.pokerstats.models.Confirmation;
import de.stammtisch.pokerstats.models.Invoice;
import de.stammtisch.pokerstats.models.PokerGame;
import de.stammtisch.pokerstats.models.Role;
import de.stammtisch.pokerstats.models.User;
import de.stammtisch.pokerstats.models.UserGame;
import de.stammtisch.pokerstats.repository.ConfirmationRepository;
import de.stammtisch.pokerstats.repository.UserRepository;

@DataJpaTest
public class ConfirmationRepositoryTest {

	@Autowired
	private ConfirmationRepository underTest;
	
	@Autowired
	private UserRepository userRepository;
	
	@AfterEach
	void teadDown() {
		underTest.deleteAll();
		userRepository.deleteAll();
	}
	
	private User generateUser() {
		Set<UserGame> games = new HashSet<UserGame>();
		Set<PokerGame> wins = new HashSet<PokerGame>();
		Set<Invoice> debts = new HashSet<Invoice>();
		Set<Invoice> credits = new HashSet<Invoice>();
		
		User user = new User();
		user.setName("Peter");
		user.setPassword("1234");
		user.setBuyIn(4);
		user.setProfilePictureType("jpg");
		user.setRole(Role.GUEST);
		user.setGames(games);
		user.setWins(wins);
		user.setDebts(debts);
		user.setCredits(credits);
		user.setEmail("peterlustig@gmail.com");
		user.setEnabled(true);
		
		return user;
	}
	
	@Test
	void canFindConfirmationByToken() {
		//given
		User user = this.generateUser();
		
		userRepository.save(user);
		
		Confirmation conf = new Confirmation();
		
		String token = UUID.randomUUID().toString();
		
		conf.setToken(token);
		conf.setValidatedAt(10);
		conf.setUser(user);
		
		underTest.save(conf);
		
		//when
		Confirmation expected = underTest.findByToken(token).orElse(null);
		
		//then
		assertThat(expected).isEqualTo(conf);
	}
}

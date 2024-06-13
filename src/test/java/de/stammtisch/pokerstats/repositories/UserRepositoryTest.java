package de.stammtisch.pokerstats.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import de.stammtisch.pokerstats.models.Invoice;
import de.stammtisch.pokerstats.models.PokerGame;
import de.stammtisch.pokerstats.models.Role;
import de.stammtisch.pokerstats.models.User;
import de.stammtisch.pokerstats.models.UserGame;
import de.stammtisch.pokerstats.repository.UserRepository;

@DataJpaTest
public class UserRepositoryTest {
	
	 @Autowired
	 private UserRepository underTest;
	 
	 @AfterEach
	 
	 void teadDown() {
		 underTest.deleteAll();
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
		 user.setEmail("peterlustig@gmail.com");
		 user.setEnabled(true);
		 
		 return user;
	 }

	 @Test
	 void canFindUserByName() {
		 //given
		 User user = this.generateUser();
		 
		 underTest.save(user);
		 
		 //when
		 User expected = underTest.findByName("Peter").orElse(null);
		 
		 //then
		 assertThat(expected).isEqualTo(user);
	 }
	 
	 @Test
	 void checkUserExistsByName() {
		 //given
		 User user = this.generateUser();
		 
		 underTest.save(user);
		 
		 //when
		 boolean expected = underTest.existsByName("Peter");
		 
		 //then
		 assertThat(expected).isEqualTo(true);
	 }
	 
	 @Test
	 void checkUserExistsByEmail() {
		 //given
		 User user = this.generateUser();
		 
		 underTest.save(user);
		 
		 //when
		 boolean expected = underTest.existsByEmail("peterlustig@gmail.com");
		 
		 //then
		 assertThat(expected).isEqualTo(true);
	 }
	 
	 @Test
	 void canFindUserByEmail() {
		 //given
		 User user = this.generateUser();
		 
		 underTest.save(user);
		 
		 //when
		 User expected = underTest.findByEmail("peterlustig@gmail.com").orElse(null);
		 
		 //then
		 assertThat(expected).isEqualTo(user);
	 }
}

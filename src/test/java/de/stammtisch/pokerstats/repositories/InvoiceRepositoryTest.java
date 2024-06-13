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
import de.stammtisch.pokerstats.repository.InvoiceRepository;
import de.stammtisch.pokerstats.repository.UserRepository;

@DataJpaTest
public class InvoiceRepositoryTest {

	@Autowired
	private InvoiceRepository underTest;
	
	@Autowired
	private UserRepository userRepository;
	
	@AfterEach
	void teadDown() {
		underTest.deleteAll();
		userRepository.deleteAll();
	}
	
	private User generateDebtor() {
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
	
	private User generateCreditor() {
		Set<UserGame> games = new HashSet<UserGame>();
		Set<PokerGame> wins = new HashSet<PokerGame>();
		Set<Invoice> debts = new HashSet<Invoice>();
		Set<Invoice> credits = new HashSet<Invoice>();
		
		User user = new User();
		user.setName("Hans");
		user.setPassword("abcd");
		user.setBuyIn(4);
		user.setProfilePictureType("jpg");
		user.setRole(Role.GUEST);
		user.setGames(games);
		user.setWins(wins);
		user.setDebts(debts);
		user.setCredits(credits);
		user.setEmail("hanslustig@gmail.com");
		user.setEnabled(true);
		
		return user;
	}
	
	@Test
	void canFindInvoicesByDebtor() {
		//given
		User debtor = this.generateDebtor();
		User creditor = this.generateCreditor();
		
		userRepository.save(debtor);
		userRepository.save(creditor);
		
		Invoice invoice1 = new Invoice();
		
		invoice1.setAmount(10);
		invoice1.setDue(10);
		invoice1.setInterest(0.5);
		invoice1.setInterestIntervalWeeks(4);
		invoice1.setDebtor(debtor);
		invoice1.setCreditor(creditor);
		invoice1.setTitle("Test");
		
		underTest.save(invoice1);
		
		//must be done to ensure no double primary keys
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
		}
		
		Invoice invoice2 = new Invoice();
		
		invoice2.setAmount(20);
		invoice2.setDue(20);
		invoice2.setInterest(2.5);
		invoice2.setInterestIntervalWeeks(2);
		invoice2.setDebtor(debtor);
		invoice2.setCreditor(creditor);
		invoice2.setTitle("Test2");
		
		underTest.save(invoice2);
		
		Set<Invoice> set = Set.of(invoice1, invoice2);
		
		//when
		Set<Invoice> expected = underTest.findByDebtor(debtor).orElse(null);
		
		//then
		assertThat(expected).isEqualTo(set);
	}
	
	@Test
	void canFindInvoicesByCreditor() {
		//given
		User debtor = this.generateDebtor();
		User creditor = this.generateCreditor();
		
		userRepository.save(debtor);
		userRepository.save(creditor);
		
		Invoice invoice1 = new Invoice();
		
		invoice1.setAmount(10);
		invoice1.setDue(10);
		invoice1.setInterest(0.5);
		invoice1.setInterestIntervalWeeks(4);
		invoice1.setDebtor(debtor);
		invoice1.setCreditor(creditor);
		invoice1.setTitle("Test");
		
		underTest.save(invoice1);
		
		//must be done to ensure no double primary keys
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
		}
		
		Invoice invoice2 = new Invoice();
		
		invoice2.setAmount(20);
		invoice2.setDue(20);
		invoice2.setInterest(2.5);
		invoice2.setInterestIntervalWeeks(2);
		invoice2.setDebtor(debtor);
		invoice2.setCreditor(creditor);
		invoice2.setTitle("Test2");
		
		underTest.save(invoice2);
		
		Set<Invoice> set = Set.of(invoice1, invoice2);
		
		//when
		Set<Invoice> expected = underTest.findByCreditor(creditor).orElse(null);
		
		//then
		assertThat(expected).isEqualTo(set);
	}
}

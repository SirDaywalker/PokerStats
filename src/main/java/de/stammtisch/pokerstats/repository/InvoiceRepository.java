package de.stammtisch.pokerstats.repository;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import de.stammtisch.pokerstats.models.Invoice;
import de.stammtisch.pokerstats.models.User;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long>{
	Optional<Set<Invoice>> findByDebtor(User debtor);
	Optional<Set<Invoice>> findByCreditor(User creditor);
}

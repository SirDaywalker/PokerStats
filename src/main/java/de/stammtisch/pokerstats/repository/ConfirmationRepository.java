package de.stammtisch.pokerstats.repository;

import de.stammtisch.pokerstats.models.Confirmation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfirmationRepository extends JpaRepository<Confirmation, Long> {

    Optional<Confirmation> findByToken(String token);

}

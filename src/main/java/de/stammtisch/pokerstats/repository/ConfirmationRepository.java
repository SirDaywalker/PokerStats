package de.stammtisch.pokerstats.repository;

import de.stammtisch.pokerstats.models.Confirmation;
import de.stammtisch.pokerstats.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConfirmationRepository extends JpaRepository<Confirmation, Long> {

    Optional<Confirmation> findByToken(String token);
    List<Confirmation> findByUser(User user);
    boolean existsByToken(String token);
}

package de.stammtisch.pokerstats.repository;

import de.stammtisch.pokerstats.models.PokerGame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PokerGameRepository extends JpaRepository<PokerGame, Long> {
    Optional<PokerGame> findByIdGreaterThan(long id);
    List<PokerGame> findAllByOrderByIdAsc();
}

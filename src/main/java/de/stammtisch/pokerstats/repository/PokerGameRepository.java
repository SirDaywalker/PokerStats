package de.stammtisch.pokerstats.repository;

import de.stammtisch.pokerstats.models.PokerGame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PokerGameRepository extends JpaRepository<PokerGame, Long> {
}

package de.stammtisch.pokerstats.repository;

import de.stammtisch.pokerstats.models.UserGame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserGameRepository extends JpaRepository<UserGame, String> {
}

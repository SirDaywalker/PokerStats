package de.stammtisch.pokerstats.repository;

import de.stammtisch.pokerstats.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByName(String name);
    boolean existsByName(String name);
    void deleteById(Long id);
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
}

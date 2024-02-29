package de.stammtisch.pokerstats.service;

import de.stammtisch.pokerstats.controller.dtos.CreatePokerGameRequest;
import de.stammtisch.pokerstats.models.PokerGame;
import de.stammtisch.pokerstats.models.User;
import de.stammtisch.pokerstats.models.UserGame;
import de.stammtisch.pokerstats.repository.PokerGameRepository;
import de.stammtisch.pokerstats.repository.UserGameRepository;
import de.stammtisch.pokerstats.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;


@Service
public class PokerGameService {
    private final PokerGameRepository pokerGameRepository;
    private final UserRepository userRepository;
    private final UserGameRepository userGameRepository;

    @Autowired
    public PokerGameService(
            PokerGameRepository pokerGameRepository,
            UserRepository userRepository,
            UserGameRepository userGameRepository
    ) {
        this.pokerGameRepository = pokerGameRepository;
        this.userRepository = userRepository;
        this.userGameRepository = userGameRepository;
    }

    public void createGame(@NonNull CreatePokerGameRequest request) {
        final PokerGame game = new PokerGame();
        final Set<User> users = new HashSet<>();
        final Set<UserGame> usersGames = new HashSet<>();

        for (String name : request.players()) {
            users.add(this.userRepository.findByName(name).orElseThrow());
        }
        if (users.size() < 2) {
            throw new NoSuchElementException("Es müssen mindestens 2 Spieler teilnehmen.");
        }

        for (User user : users) {
            UserGame userGame = new UserGame();
            userGame.setUser(user);
            userGame.setPokerGame(game);
            userGame.setBuyIn(user.getBuyIn());
            usersGames.add(userGame);
        }
        game.setNotes(request.notes());
        game.setUsers(usersGames);
        game.setWinner(null);
        this.pokerGameRepository.save(game);
        this.userGameRepository.saveAll(usersGames);
    }

    public List<PokerGame> getGames() {
        return this.pokerGameRepository.findAll();
    }

    public double getCurrentGamePot() {
        double pot = 0;

        for (PokerGame game : this.pokerGameRepository.findAll()) {
            for (UserGame userGame : game.getUsers()) {
                pot += userGame.getBuyIn();
            }
            pot /= 2;
        }
        // Round pot to 2 decimal places
        return Math.round(pot * 100.0) / 100.0;
    }
}

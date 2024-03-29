package de.stammtisch.pokerstats.service;

import de.stammtisch.pokerstats.controller.dtos.CreatePokerGameRequest;
import de.stammtisch.pokerstats.models.PokerGame;
import de.stammtisch.pokerstats.models.User;
import de.stammtisch.pokerstats.models.UserGame;
import de.stammtisch.pokerstats.repository.PokerGameRepository;
import de.stammtisch.pokerstats.repository.UserGameRepository;
import de.stammtisch.pokerstats.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;


@Service
@AllArgsConstructor
public class PokerGameService {
    private final PokerGameRepository pokerGameRepository;
    private final UserRepository userRepository;
    private final UserGameRepository userGameRepository;

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

    public void updateWinner(Long gameId, Long userId) {
        PokerGame game = this.pokerGameRepository.findById(gameId).orElseThrow();

        if (userId == null) {
            game.setWinner(null);
        } else {
            User user = this.userRepository.findById(userId).orElseThrow();
            game.setWinner(user);
        }
        this.pokerGameRepository.save(game);
    }

    public List<PokerGame> getGames() {
        List<PokerGame> games = this.pokerGameRepository.findAll();

        // Sort games by id
        games.sort((a, b) -> (int) (a.getId() - b.getId()));
        return games;
    }

    public double getCurrentPot() {
        if (this.getGames().isEmpty()) {
            return 0;
        }
        double pot = this.getPot(this.getGames().get(this.getGames().size() - 1)) / 2;
        pot = Math.round(pot * 100.0) / 100.0;
        return pot;
    }

    public double getPot(PokerGame game) {
        List<PokerGame> games = this.getGames();

        double pot = 0;
        for (PokerGame g : games.subList(0, games.indexOf(game) + 1)) {
            pot = Math.round(pot * 100.0) / 100.0;
            pot /= 2;
            for (UserGame userGame : g.getUsers()) {
                pot += userGame.getBuyIn();
            }
        }
        return pot;
    }
}

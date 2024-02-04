package de.stammtisch.pokerstats.service;

import de.stammtisch.pokerstats.controllers.dtos.CreatePokerGameRequest;
import de.stammtisch.pokerstats.models.PokerGame;
import de.stammtisch.pokerstats.models.User;
import de.stammtisch.pokerstats.models.UserGame;
import de.stammtisch.pokerstats.repository.PokerGameRepository;
import de.stammtisch.pokerstats.repository.UserGameRepository;
import de.stammtisch.pokerstats.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
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

    public void createGame(CreatePokerGameRequest request) {
        final PokerGame game = new PokerGame();
        final Set<User> users = new HashSet<>();
        final Set<UserGame> usersGames = new HashSet<>();

        for (String name : request.players()) {
            users.add(this.userRepository.findByName(name).orElseThrow());
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
        game.setWinner(this.userRepository.findByName(request.winner()).orElseThrow());
        this.pokerGameRepository.save(game);
        this.userGameRepository.saveAll(usersGames);
    }
}

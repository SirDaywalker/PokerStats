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

import java.util.*;


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

    /**
     * Updates the winner of the game with the given id. If the winnerId is null, the game is set to running.
     * @param gameId the id of the game to update.
     * @param winnerId the id of the winner.
     */
    public void updateWinner(long gameId, Long winnerId) {
        PokerGame game = this.pokerGameRepository.findById(gameId).orElseThrow();

        if (winnerId == null) {
            game.setWinner(null);
        } else {
            User winner = this.userRepository.findById(winnerId).orElseThrow();
            game.setWinner(winner);
        }
        this.pokerGameRepository.save(game);
    }

    /**
     * Updates the players of the game with the given id. The players are set to the players with the given ids. A
     * game can only be updated if it is the last game in the list, as the pot is calculated based on the last game.
     * @param gameId the id of the game to update.
     * @param playerIds the ids of the players. Should contain at least 2 unique ids. Should to be null.
     */
    public void updatePlayers(long gameId, @NonNull long[] playerIds) {
        if (playerIds.length < 2) {
            throw new IllegalArgumentException("Es müssen mindestens 2 Spieler teilnehmen.");
        }
        if (Arrays.stream(playerIds).distinct().count() < playerIds.length) {
            throw new IllegalArgumentException("Ein Spieler kann nicht mehrfach teilnehmen.");
        }
        if (this.isNotLastGame(gameId)) {
            throw new IllegalArgumentException("Die Spieler können nur im letzten Spiel geändert werden.");
        }

        PokerGame game = this.pokerGameRepository.findById(gameId).orElseThrow();
        Set<UserGame> usersGames = game.getUsers();

        for (UserGame userGame : usersGames) {
            // If the user is not in the new list of players, remove them from the game
            if (Arrays.stream(playerIds).noneMatch(id -> id == userGame.getUser().getId())) {
                this.userGameRepository.delete(userGame);
            }
        }

        for (long playerId : playerIds) {
            // If the user is not in the current list of players, add them to the game
            if (usersGames.stream().noneMatch(userGame -> userGame.getUser().getId() == playerId)) {
                User user = this.userRepository.findById(playerId).orElseThrow();
                UserGame userGame = new UserGame();
                userGame.setUser(user);
                userGame.setPokerGame(game);
                userGame.setBuyIn(user.getBuyIn());
                usersGames.add(userGame);
            }
        }
        game.setUsers(usersGames);
        this.pokerGameRepository.save(game);
    }

    /**
     * Returns a list of all games. The games are sorted by date (id).
     * @return a list of all games.
     */
    public List<PokerGame> getGames() {
        return this.pokerGameRepository.findAllByOrderByIdAsc();
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

    /**
     * Checks if the game with the given id is the last game in the list.
     * @param gameId the id of the game to check.
     * @return true if the game is the last game in the list, false otherwise.
     */
    public boolean isNotLastGame(long gameId) {
        return this.pokerGameRepository.findByIdGreaterThan(gameId).isPresent();
    }

    /**
     * Deletes the game with the given id if it is the last game in the list.
     * @param gameId the id of the game to delete.
     * @throws IllegalArgumentException if the game is not the last game in the list.
     */
    public void deleteGame(long gameId) {
        if (this.isNotLastGame(gameId)) {
            throw new IllegalArgumentException(
                    "Das Spiel kann nicht gelöscht werden, da es nicht das letzte Spiel ist."
            );
        }
        PokerGame game = this.pokerGameRepository.findById(gameId).orElseThrow();
        this.pokerGameRepository.delete(game);
    }
}

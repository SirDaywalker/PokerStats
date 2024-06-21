package de.stammtisch.pokerstats.controller.api;

import com.google.gson.Gson;
import de.stammtisch.pokerstats.controller.dtos.CreatePokerGameRequest;
import de.stammtisch.pokerstats.controller.dtos.UpdatePokerGamePlayersRequest;
import de.stammtisch.pokerstats.controller.dtos.UpdatePokerGameWinnerRequest;
import de.stammtisch.pokerstats.models.PokerGame;
import de.stammtisch.pokerstats.models.UserGame;
import de.stammtisch.pokerstats.service.PokerGameService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/v1/games/poker")
@AllArgsConstructor
public class PokerGameController {
    private final PokerGameService pokerGameService;

    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestBody CreatePokerGameRequest request) {
        try {
            this.pokerGameService.createGame(request);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Successfully created Poker Game.", HttpStatus.CREATED);
    }

    @PostMapping("/set-winner")
    public ResponseEntity<String> update(@NonNull @RequestBody UpdatePokerGameWinnerRequest request) {
        try {
            this.pokerGameService.updateWinner(request.gameId(), request.winnerId());
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(
                    "Spieler oder Spiel konnte nicht gefunden werden",
                    HttpStatus.BAD_REQUEST
            );
        }
        return new ResponseEntity<>("Gewinner erfolgreich aktualisiert.", HttpStatus.OK);
    }

    @PostMapping("/set-players")
    public ResponseEntity<String> setPlayers(@NonNull @RequestBody UpdatePokerGamePlayersRequest request) {
        if (request.playerIds() == null) {
            return new ResponseEntity<>("Keine Spieler angegeben.", HttpStatus.BAD_REQUEST);
        }
        try {
            this.pokerGameService.updatePlayers(request.gameId(), request.playerIds());
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Successfully added player to Poker Game.", HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@RequestParam(name = "id") long gameId) {
        try {
            this.pokerGameService.deleteGame(gameId);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(
                    "Das gesuchte Spiel konnte nicht gefunden werden.", HttpStatus.BAD_REQUEST
            );
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Das Spiel wurde erfolgreich gelöscht.", HttpStatus.OK);
    }

    @GetMapping("/stats")
    public ResponseEntity<String> stats() {
        List<PokerGame> games = this.pokerGameService.getGames();
        Map<String, Map<Long, Map<String, Object>>> stats = new HashMap<>();
        Map<Long, Map<String, Object>> gameStats = new HashMap<>();
        Map<Long, Map<String, Object>> winnerStats = new HashMap<>();

        for (PokerGame game : games) {
            double pot = this.pokerGameService.getPot(game);
            double payout = Math.round((pot / 2) * 100.0) / 100.0;

            Map<Long, String> users = new HashMap<>();
            for (UserGame userGame : game.getUsers()) {
                users.put(userGame.getUser().getId(), userGame.getUser().getUsername());
            }
            gameStats.put(
                    game.getId(),
                    Map.of("pot", pot, "payout", payout, "users", users)
            );

            Long winnerId;
            try {
                winnerId = game.getWinner().getId();
            } catch (NullPointerException e) {
                continue;
            }
            if (!winnerStats.containsKey(winnerId)) {
                winnerStats.put(
                        winnerId,
                        Map.of(
                                "wins", 1,
                                "name", game.getWinner().getUsername(),
                                "payout", payout
                        )
                );
            } else {
                int wins = (int) winnerStats.get(winnerId).get("wins");
                double payoutSum = (double) winnerStats.get(winnerId).get("payout");
                winnerStats.put(
                        winnerId,
                        Map.of(
                                "wins", wins + 1,
                                "name", game.getWinner().getUsername(),
                                "payout", payoutSum + payout
                        )
                );
            }
        }
        stats.put("games", gameStats);
        stats.put("winners", winnerStats);

        Gson gson = new Gson();
        String json = gson.toJson(stats);
        return new ResponseEntity<>(json, HttpStatus.OK);
    }
}

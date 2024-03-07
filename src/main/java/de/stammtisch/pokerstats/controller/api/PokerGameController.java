package de.stammtisch.pokerstats.controller.api;

import com.google.gson.Gson;
import de.stammtisch.pokerstats.controller.dtos.CreatePokerGameRequest;
import de.stammtisch.pokerstats.models.PokerGame;
import de.stammtisch.pokerstats.models.UserGame;
import de.stammtisch.pokerstats.service.PokerGameService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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

    @GetMapping("/stats")
    public ResponseEntity<String> stats() {
        List<PokerGame> games = this.pokerGameService.getGames();
        Map<Integer, Map<Object, Object>> stats = new HashMap<>();

        for (PokerGame game : games) {
            double pot = this.pokerGameService.getPot(game);
            double payout = Math.round((pot / 2) * 100.0) / 100.0;

            ArrayList<String> users = new ArrayList<>();
            for (UserGame userGame : game.getUsers()) {
                users.add(userGame.getUser().getUsername());
            }
            stats.put(
                    games.indexOf(game),
                    Map.of("pot", pot, "payout", payout, "users", users)
            );
        }
        Gson gson = new Gson();
        String json = gson.toJson(stats);
        return new ResponseEntity<>(json, HttpStatus.OK);
    }
}

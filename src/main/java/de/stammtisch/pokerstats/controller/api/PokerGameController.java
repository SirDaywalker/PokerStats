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

import java.util.ArrayList;
import java.util.List;
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

    @GetMapping("/stats")
    public ResponseEntity<String> stats() {
        List<PokerGame> games = this.pokerGameService.getGames();
        final double[] pots = new double[games.size()];
        final double[] payouts = new double[games.size()];
        final ArrayList<String[]> users = new ArrayList<>(games.size());

        for (int i = 0; i < games.size(); i++) {
            PokerGame game = games.get(i);

            double pot = this.pokerGameService.getPot(game);
            pots[i] = pot;
            payouts[i] = Math.round((pot / 2) * 100.0) / 100.0;

            ArrayList<String> gameUsers = new ArrayList<>();
            for (UserGame userGame : game.getUsers()) {
                gameUsers.add(userGame.getUser().getUsername());
            }
            users.add(gameUsers.toArray(new String[0]));
        }
        Gson gson = new Gson();
        return new ResponseEntity<>(gson.toJson(new Object[]{pots, payouts, users}), HttpStatus.OK);
    }
}

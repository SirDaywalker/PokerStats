package de.stammtisch.pokerstats.controller.api;

import de.stammtisch.pokerstats.controller.dtos.CreatePokerGameRequest;
import de.stammtisch.pokerstats.service.PokerGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/v1/poker-game")
public class PokerGameController {
    private final PokerGameService pokerGameService;

    @Autowired
    public PokerGameController(PokerGameService pokerGameService) {
        this.pokerGameService = pokerGameService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestBody CreatePokerGameRequest request) {
        try {
            this.pokerGameService.createGame(request);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Successfully created Poker Game.", HttpStatus.CREATED);
    }
}

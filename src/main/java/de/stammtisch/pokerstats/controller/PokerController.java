package de.stammtisch.pokerstats.controller;

import de.stammtisch.pokerstats.models.User;
import de.stammtisch.pokerstats.service.AuthenticationService;
import de.stammtisch.pokerstats.service.PokerGameService;
import de.stammtisch.pokerstats.service.UserService;
import io.jsonwebtoken.JwtException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

@Controller
@RequestMapping("/games/poker")
@AllArgsConstructor
public class PokerController {
    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final PokerGameService pokerGameService;

    @GetMapping("/")
    public ModelAndView poker(@RequestHeader(name = "Cookie") String cookies) {
        ModelAndView modelAndView = new ModelAndView("poker-games");
        try {
            User account = this.authenticationService.getUserFromToken(
                    this.authenticationService.getTokenFromCookie(cookies)
            );
            modelAndView.addObject("account", account);
        } catch (IllegalArgumentException | JwtException | NoSuchElementException e) {
            modelAndView.setViewName("login");
            return modelAndView;
        }
        modelAndView.addObject("games", this.pokerGameService.getGames());
        final double pot = this.pokerGameService.getCurrentGamePot();
        modelAndView.addObject("pot", pot);
        return modelAndView;
    }

    @GetMapping("/new")
    public ModelAndView newPokerGame(@RequestHeader(name = "Cookie") String cookies) {
        ModelAndView modelAndView = new ModelAndView("new-poker-game");
        try {
            User account = this.authenticationService.getUserFromToken(
                    this.authenticationService.getTokenFromCookie(cookies)
            );
            modelAndView.addObject("account", account);
        } catch (IllegalArgumentException | JwtException | NoSuchElementException e) {
            modelAndView.setViewName("login");
            return modelAndView;
        }
        final double pot = this.pokerGameService.getCurrentGamePot();
        final List<User> users = this.userService.getAllUsers();
        modelAndView.addObject("users", users);
        modelAndView.addObject("pot", pot);
        return modelAndView;
    }

    @GetMapping("/pot")
    public ModelAndView pot(@RequestHeader(name = "Cookie") String cookies) {
        ModelAndView modelAndView = new ModelAndView("pot");
        try {
            User account = this.authenticationService.getUserFromToken(
                    this.authenticationService.getTokenFromCookie(cookies)
            );
            modelAndView.addObject("account", account);
        } catch (IllegalArgumentException | JwtException | NoSuchElementException e) {
            modelAndView.setViewName("login");
            return modelAndView;
        }
        final int size = this.pokerGameService.getGames().size();
        final double[] potProgress = new double[size];
        final int[] playerAmount = new int[size];

        for (int i = 0; i < size; i++) {
            final double pot = this.pokerGameService.getCurrentGamePot(i);
            potProgress[i] = pot;
            playerAmount[i] = this.pokerGameService.getGames().get(i).getUsers().size();
        }
        double pot = this.pokerGameService.getCurrentGamePot();

        modelAndView.addObject("potProgress", Arrays.toString(potProgress));
        modelAndView.addObject("playerAmount", Arrays.toString(playerAmount));
        modelAndView.addObject("size", size);
        modelAndView.addObject("pot", pot);
        return modelAndView;
    }
}

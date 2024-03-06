package de.stammtisch.pokerstats.controller;

import de.stammtisch.pokerstats.models.User;
import de.stammtisch.pokerstats.service.AuthenticationService;
import de.stammtisch.pokerstats.service.PokerGameService;
import de.stammtisch.pokerstats.service.UserService;
import io.jsonwebtoken.JwtException;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

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
        processDefault(modelAndView, cookies);
        modelAndView.addObject("games", this.pokerGameService.getGames());
        return modelAndView;
    }

    @GetMapping("/new")
    public ModelAndView newPokerGame(@RequestHeader(name = "Cookie") String cookies) {
        ModelAndView modelAndView = new ModelAndView("new-poker-game");
        processDefault(modelAndView, cookies);
        final List<User> users = this.userService.getAllUsers();
        modelAndView.addObject("users", users);
        return modelAndView;
    }

    @GetMapping("/pot")
    public ModelAndView pot(@RequestHeader(name = "Cookie") String cookies) {
        ModelAndView modelAndView = new ModelAndView("pot");
        processDefault(modelAndView, cookies);
        return modelAndView;
    }

    private void processDefault(@NonNull ModelAndView modelAndView, String cookies) {
        try {
            User account = this.authenticationService.getUserFromToken(
                    this.authenticationService.getTokenFromCookie(cookies)
            );
            modelAndView.addObject("account", account);
        } catch (IllegalArgumentException | JwtException | NoSuchElementException e) {
            modelAndView.setViewName("login");
            return;
        }
        final double pot = this.pokerGameService.getCurrentPot();
        modelAndView.addObject("pot", pot);
    }

}

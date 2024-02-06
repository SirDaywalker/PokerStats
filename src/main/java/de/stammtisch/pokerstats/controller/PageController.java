package de.stammtisch.pokerstats.controller;

import de.stammtisch.pokerstats.models.User;
import de.stammtisch.pokerstats.service.AuthenticationService;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.servlet.ModelAndView;

import java.util.NoSuchElementException;

@Controller
public class PageController {
    private final AuthenticationService authenticationService;

    @Autowired
    public PageController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @GetMapping("/")
    public ModelAndView landing(@RequestHeader(name = "Cookie", required = false) String cookies) {
        User user = null;
        try {
            user = this.authenticationService.getUserFromToken(this.authenticationService.getTokenFromCookie(cookies));
        } catch (IllegalArgumentException | JwtException | NoSuchElementException ignored) {}

        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("user", user);
        return modelAndView;
    }

    @GetMapping("/home")
    public ModelAndView home(@RequestHeader(name = "Cookie") String cookies) {
        ModelAndView modelAndView = new ModelAndView("home");
        try {
            User user = this.authenticationService.getUserFromToken(this.authenticationService.getTokenFromCookie(cookies));
            modelAndView.addObject("user", user);
        } catch (IllegalArgumentException | JwtException | NoSuchElementException e) {
            modelAndView.setViewName("login");
        }
        return modelAndView;
    }

    @GetMapping("/account")
    public ModelAndView account(@RequestHeader(name = "Cookie") String cookies) {
        ModelAndView modelAndView = new ModelAndView("account");
        try {
            User user = this.authenticationService.getUserFromToken(this.authenticationService.getTokenFromCookie(cookies));
            modelAndView.addObject("user", user);
        } catch (IllegalArgumentException | JwtException | NoSuchElementException e) {
            modelAndView.setViewName("login");
        }
        return modelAndView;
    }

    @GetMapping("/new-poker-game")
    public ModelAndView newPokerGame(@RequestHeader(name = "Cookie") String cookies) {
        ModelAndView modelAndView = new ModelAndView("new-poker-game");
        try {
            User user = this.authenticationService.getUserFromToken(this.authenticationService.getTokenFromCookie(cookies));
            modelAndView.addObject("user", user);
        } catch (IllegalArgumentException | JwtException | NoSuchElementException e) {
            modelAndView.setViewName("login");
        }
        return modelAndView;
    }

    @GetMapping("/statistics")
    public ModelAndView statistics(@RequestHeader(name = "Cookie") String cookies) {
        ModelAndView modelAndView = new ModelAndView("statistics");
        try {
            User user = this.authenticationService.getUserFromToken(this.authenticationService.getTokenFromCookie(cookies));
            modelAndView.addObject("user", user);
        } catch (IllegalArgumentException | JwtException | NoSuchElementException e) {
            modelAndView.setViewName("login");
        }
        return modelAndView;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/error")
    public String error() {
        return "error";
    }
}

package de.stammtisch.pokerstats.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PageController {

    @GetMapping("/")
    public ModelAndView landing(@RequestHeader(name = "Cookie", required = false) String cookies) {
        ModelAndView modelAndView = new ModelAndView("landing");
        return modelAndView;
    }

    @GetMapping("/home")
    public ModelAndView home(@RequestHeader(name = "Cookie") String cookies) {
        ModelAndView modelAndView = new ModelAndView("home");
        return modelAndView;
    }

    @GetMapping("/account")
    public ModelAndView account(@RequestHeader(name = "Cookie") String cookies) {
        ModelAndView modelAndView = new ModelAndView("account");
        return modelAndView;
    }

    @GetMapping("/new-poker-game")
    public ModelAndView newPokerGame() {
        ModelAndView modelAndView = new ModelAndView("new-poker-game");
        return modelAndView;
    }

    @GetMapping("/statistics")
    public ModelAndView statistics() {
        ModelAndView modelAndView = new ModelAndView("statistics");
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

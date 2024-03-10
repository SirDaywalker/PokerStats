package de.stammtisch.pokerstats.controller;

import de.stammtisch.pokerstats.exceptions.ConfirmationTimeExceededException;
import de.stammtisch.pokerstats.exceptions.UserAlreadyEnabledException;
import de.stammtisch.pokerstats.models.PokerGame;
import de.stammtisch.pokerstats.models.Role;
import de.stammtisch.pokerstats.models.User;
import de.stammtisch.pokerstats.service.AuthenticationService;
import de.stammtisch.pokerstats.service.ConfirmationService;
import de.stammtisch.pokerstats.service.PokerGameService;
import de.stammtisch.pokerstats.service.UserService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Controller
@AllArgsConstructor
public class PageController {
    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final PokerGameService pokerGameService;
    private final ConfirmationService confirmationService;

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
            final User user = this.authenticationService.getUserFromToken(this.authenticationService.getTokenFromCookie(cookies));
            modelAndView.addObject("user", user);
        } catch (UsernameNotFoundException e) {
            modelAndView.setViewName("error");
        }
        return getModelAndView(cookies, modelAndView);
    }

    @GetMapping("/account")
    public ModelAndView account(
            @RequestHeader(name = "Cookie") String cookies,
            @RequestParam(name = "u", required = false) Long requestedUserId
    ) {
        ModelAndView modelAndView = new ModelAndView("account");

        final User account;
        try {
            account = this.authenticationService.getUserFromToken(this.authenticationService.getTokenFromCookie(cookies));
            modelAndView.addObject("account", account);
        } catch (IllegalArgumentException | JwtException | NoSuchElementException e) {
            modelAndView.setViewName("login");
            return modelAndView;
        }

        if (requestedUserId != null) {
            if (!account.getRole().equals(Role.ADMIN)) {
                modelAndView.setViewName("error");
                return modelAndView;
            }
            try {
                final User user = this.userService.getUserById(requestedUserId);
                modelAndView.addObject("user", user);
            } catch (UsernameNotFoundException e) {
                modelAndView.setViewName("error");
            }
        } else {
            modelAndView.addObject("user", account);
        }
        final double pot = this.pokerGameService.getCurrentPot();
        modelAndView.addObject("pot", pot);
        return modelAndView;
    }

    @GetMapping("/statistics")
    public ModelAndView statistics(@RequestHeader(name = "Cookie") String cookies) {
        ModelAndView modelAndView = new ModelAndView("statistics");

        final User account;
        try {
            account = this.authenticationService.getUserFromToken(
                    this.authenticationService.getTokenFromCookie(cookies)
            );
            modelAndView.addObject("account", account);
        } catch (IllegalArgumentException | JwtException | NoSuchElementException e) {
            modelAndView.setViewName("login");
            return modelAndView;
        }

        List<PokerGame> games = pokerGameService.getGames();
        List<PokerGame> latestGames = new ArrayList<>();

        for (PokerGame game : games) {
            if (game.userHasPlayed(account)) {
                latestGames.add(game);
            }
            if (latestGames.size() > 3) {
                latestGames.remove(0);
            }
        }
        modelAndView.addObject("latestGames", latestGames);
        final double pot = this.pokerGameService.getCurrentPot();
        modelAndView.addObject("pot", pot);
        return modelAndView;
    }

    private ModelAndView getModelAndView(
            @RequestHeader(name = "Cookie") String cookies,
            @NonNull ModelAndView modelAndView
    ) {
        try {
            User account = this.authenticationService.getUserFromToken(
                    this.authenticationService.getTokenFromCookie(cookies)
            );
            modelAndView.addObject("account", account);
        } catch (IllegalArgumentException | JwtException | NoSuchElementException e) {
            modelAndView.setViewName("login");
        }
        final double pot = this.pokerGameService.getCurrentPot();
        modelAndView.addObject("pot", pot);
        return modelAndView;
    }

    @GetMapping("/login")
    public String login(@RequestParam(name = "logout", required = false) String logout, HttpServletResponse response) {
        if (logout != null) {
            response.addCookie(AuthenticationService.generateCookie(null));
        }
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

    @GetMapping("/create-invoice")
    public ModelAndView createInvoice(@RequestHeader(name = "Cookie") String cookies) {
        ModelAndView modelAndView = new ModelAndView("create-invoice");
        try {
            User user = this.authenticationService.getUserFromToken(this.authenticationService.getTokenFromCookie(cookies));
            modelAndView.addObject("account", user);
        } catch (IllegalArgumentException | JwtException | NoSuchElementException e) {
            modelAndView.setViewName("login");
            return modelAndView;
        }
        final List<User> users = this.userService.getAllUsers();
        modelAndView.addObject("users", users);
        return modelAndView;
    }

    @GetMapping("/confirm-invoice")
    public String confirmInvoice() {
        return "confirm-invoice";
    }

    @GetMapping("/request-password-reset")
    public String requestPasswordReset() {
        return "request-password-reset";
    }

    @GetMapping("/request-confirmation")
    public String requestConfirmation() {
    	return "request-confirmation"; 
    }
    
    @GetMapping("/confirm")
    public ModelAndView confirm(@RequestParam("confirmation") String confirmation) {
    	ModelAndView modelAndView = new ModelAndView("redirect:/home");
    	try {
    		this.confirmationService.confirmUser(confirmation);
    	} catch (ConfirmationTimeExceededException | NoSuchElementException e) {
    		modelAndView.setViewName("redirect:/request-confirmation");
    	} catch (UserAlreadyEnabledException ignored) {
    	}
		return modelAndView;
    }
    
    @GetMapping("confirm-redirect")
    public ModelAndView confirmRedirect(@RequestParam("confirmation") String confirmation) {
    	ModelAndView modelAndView = new ModelAndView("redirection");
    	modelAndView.addObject("confirmation", confirmation);
    	return modelAndView;
    }

    @GetMapping("/password-reset")
    public String passwordReset() {
    	return "password-reset";
    }

    @GetMapping("/users")
    public ModelAndView selectUser(@RequestHeader(name = "Cookie") String cookies) {
        ModelAndView modelAndView = new ModelAndView("users");
        try {
            final User account = this.authenticationService.getUserFromToken(this.authenticationService.getTokenFromCookie(cookies));
            modelAndView.addObject("account", account);
        } catch (UsernameNotFoundException e) {
            modelAndView.setViewName("error");
        }
        final List<User> users = this.userService.getAllUsers();
        modelAndView.addObject("users", users);

        final double pot = this.pokerGameService.getCurrentPot();
        modelAndView.addObject("pot", pot);
        return modelAndView;
    }
}

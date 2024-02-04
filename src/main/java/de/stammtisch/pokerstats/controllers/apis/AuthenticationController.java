package de.stammtisch.pokerstats.controllers.apis;

import de.stammtisch.pokerstats.controllers.dtos.AuthenticationRequest;
import de.stammtisch.pokerstats.controllers.dtos.RegisterRequest;
import de.stammtisch.pokerstats.service.AuthenticationService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterRequest request, HttpServletResponse response) {
        final String token;
        try {
            token = this.authenticationService.register(request);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        response.addCookie(AuthenticationService.generateCookie(token));
        return new ResponseEntity<>("Successfully registered.", HttpStatus.OK);
    }

    @PostMapping("authenticate")
    public ResponseEntity<String> authenticate (
            @RequestBody AuthenticationRequest request,
            HttpServletResponse response
    ) {
        final String token;
        try {
            token = this.authenticationService.authenticate(request);
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        response.addCookie(AuthenticationService.generateCookie(""));
        return new ResponseEntity<>("Successfully authenticated.", HttpStatus.OK);
    }
}

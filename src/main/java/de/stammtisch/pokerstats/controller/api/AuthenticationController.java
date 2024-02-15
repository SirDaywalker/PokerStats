package de.stammtisch.pokerstats.controller.api;

import de.stammtisch.pokerstats.controller.dtos.AuthenticationRequest;
import de.stammtisch.pokerstats.controller.dtos.EditAccountRequest;
import de.stammtisch.pokerstats.controller.dtos.RegisterRequest;
import de.stammtisch.pokerstats.service.AuthenticationService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@ModelAttribute RegisterRequest request, HttpServletResponse response) {
        final String token;
        try {
            token = this.authenticationService.register(request);
        } catch (IllegalArgumentException | NullPointerException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (IOException e) {
            return new ResponseEntity<>("An error occurred while processing the request.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.addCookie(AuthenticationService.generateCookie(token));
        return new ResponseEntity<>("Successfully registered.", HttpStatus.OK);
    }

    @PostMapping("/authenticate")
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
        response.addCookie(AuthenticationService.generateCookie(token));
        return new ResponseEntity<>("Successfully authenticated.", HttpStatus.OK);
    }

    @PostMapping("/change-details")
    public ResponseEntity<String> changeDetails(
            @RequestBody EditAccountRequest request,
            @RequestParam(required = false) MultipartFile picture,
            @RequestHeader("Cookie") String cookies,
            HttpServletResponse response
    ) {
        final String token;
        try {
            token = this.authenticationService.changeDetails(request, cookies, picture);
        } catch (IllegalArgumentException | JwtException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (IOException e) {
            return new ResponseEntity<>("An error occurred while processing the request.", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        response.addCookie(AuthenticationService.generateCookie(token));
        return new ResponseEntity<>("Successfully changed details.", HttpStatus.OK);
    }
}

package de.stammtisch.pokerstats.controller.api;

import java.io.IOException;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.stammtisch.pokerstats.controller.dtos.AuthenticationRequest;
import de.stammtisch.pokerstats.controller.dtos.EditAccountRequest;
import de.stammtisch.pokerstats.controller.dtos.PasswordOrConfirmationRequest;
import de.stammtisch.pokerstats.controller.dtos.RegisterRequest;
import de.stammtisch.pokerstats.exceptions.EmailAlreadyInUseException;
import de.stammtisch.pokerstats.exceptions.InvalidRequestParameterException;
import de.stammtisch.pokerstats.exceptions.UserAlreadyEnabledException;
import de.stammtisch.pokerstats.exceptions.UserNotEnabledException;
import de.stammtisch.pokerstats.service.AuthenticationService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@ModelAttribute RegisterRequest request, HttpServletResponse response) {
        final String token;
        try {
            token = this.authenticationService.register(request);
        } catch (IllegalArgumentException | NullPointerException e) {
            return new ResponseEntity<>("Die Anfrage konnte nicht verarbeitet werden.", HttpStatus.BAD_REQUEST);
        } catch (IOException e) {
            return new ResponseEntity<>("Ein Fehler ist aufgetreten.", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (EmailAlreadyInUseException e) {
            return new ResponseEntity<>("Die E-Mail-Adresse ist bereits in Benutzung.", HttpStatus.BAD_REQUEST);
        } catch (InvalidRequestParameterException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        response.addCookie(AuthenticationService.generateCookie(token));
        return new ResponseEntity<>("Erfolgreich registriert.", HttpStatus.OK);
    }

    @PutMapping("/request-confirmation")
    public ResponseEntity<String> requestConfirmation(@RequestBody PasswordOrConfirmationRequest request, HttpServletResponse response){
        final String token;
        try {
        	token = this.authenticationService.requestConfirmation(request);
        } catch (UserAlreadyEnabledException e) {
        	return new ResponseEntity<>("Benutzer Email wurde bereits best&aumltigt.", HttpStatus.BAD_REQUEST);
        } catch (NoSuchElementException e) {
    		return new ResponseEntity<>("Benutzer konnte nicht gefunden werden.", HttpStatus.BAD_REQUEST);
    	}
        
        response.addCookie(AuthenticationService.generateCookie(token));
		return new ResponseEntity<>("Best&aumltigunsmail wurde gesendet.", HttpStatus.OK);
    }

    @PutMapping("/request-password-reset")
    public ResponseEntity<String> resetPassword(@RequestBody PasswordOrConfirmationRequest request, HttpServletResponse response){
    	final String token;
        try {
        	token = this.authenticationService.requestPasswordReset(request);
        } catch (NoSuchElementException e) {
    		return new ResponseEntity<>("Benutzer konnte nicht gefunden werden.", HttpStatus.BAD_REQUEST);
    	} catch (UserNotEnabledException e) {
    		return new ResponseEntity<>("Benutzer Email wurde noch nicht best&aumltigt.", HttpStatus.BAD_REQUEST);
    	}
        
        response.addCookie(AuthenticationService.generateCookie(token));
		return new ResponseEntity<>("Mail zur Passwortzur&uumlcksetzung wurde gesendet.", HttpStatus.OK);
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
            return new ResponseEntity<>("Die Anmeldedaten sind falsch.", HttpStatus.UNAUTHORIZED);
        } catch (DisabledException e) {
            return new ResponseEntity<>(
                    "Der Account ist entweder nicht best&aumltigt oder gesperrt. Bitte &uumlberpr&uumlfen Sie Ihre E-Mails.",
                    HttpStatus.UNAUTHORIZED
            );
        }
        response.addCookie(AuthenticationService.generateCookie(token));
        return new ResponseEntity<>("Erfolgreich angemeldet.", HttpStatus.OK);
    }

    @PostMapping("/change-details")
    public ResponseEntity<String> changeDetails(
            @ModelAttribute EditAccountRequest request,
            @RequestHeader("Cookie") String cookies,
            HttpServletResponse response
    ) {
        final String token;
        try {
            token = this.authenticationService.changeDetails(request, cookies);
        } catch (IllegalArgumentException | JwtException | NoSuchElementException e) {
            return new ResponseEntity<>("Die Anfrage konnte nicht verarbeitet werden.", HttpStatus.BAD_REQUEST);
        } catch (IOException e) {
            return new ResponseEntity<>("Ein Fehler ist aufgetreten.", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>("Das Passwort ist falsch.", HttpStatus.UNAUTHORIZED);
        } catch (EmailAlreadyInUseException e) {
            return new ResponseEntity<>("Die E-Mail-Adresse ist bereits in Benutzung.", HttpStatus.BAD_REQUEST);
        }
        response.addCookie(AuthenticationService.generateCookie(token));
        return new ResponseEntity<>("Erfolgreich ge&aumlndert.", HttpStatus.OK);
    }
}

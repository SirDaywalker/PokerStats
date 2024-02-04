package de.stammtisch.pokerstats.service;

import de.stammtisch.pokerstats.controllers.dtos.AuthenticationRequest;
import de.stammtisch.pokerstats.models.User;
import de.stammtisch.pokerstats.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final JWTService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    @Autowired
    public AuthenticationService(
            JWTService jwtService,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            UserRepository userRepository
    ) {
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
    }

    /**
     * Generates the Cookie to safe the authentication to access the site.
     *
     * @param token the token
     * @return the freshly baked cookie.
     */
    public static @NonNull Cookie generateCookie(String token) {
        Cookie cookie = new Cookie("Authorization", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 24 * 7);
        cookie.setAttribute("SameSite", "Strict");
        return cookie;
    }

    /**
     * Authenticates the user.
     *
     * @param authenticationRequest The authentication request.
     * @return The generated token.
     */
    public String authenticate(@NonNull AuthenticationRequest authenticationRequest) {
        this.matchCredentials(authenticationRequest.name(), authenticationRequest.password());
        final User user = this.userRepository.findByName(authenticationRequest.name()).orElseThrow();
        return this.generateToken(user);
    }

    /**
     * Matches the credentials against the database.
     *
     * @param name The name to match.
     * @param password The password to match.
     * @throws BadCredentialsException If the credentials are not valid.
     */
    public void matchCredentials(String name, String password) throws BadCredentialsException {
        this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        name,
                        password
                )
        );
    }

    /**
     * Generates a token for the given user.
     *
     * @param user The user to generate the token for.
     * @return The generated token.
     */
    public String generateToken(@NonNull User user) {
        return this.jwtService.generateToken(user);
    }
}

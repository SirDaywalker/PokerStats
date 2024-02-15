package de.stammtisch.pokerstats.service;

import de.stammtisch.pokerstats.controller.dtos.AuthenticationRequest;
import de.stammtisch.pokerstats.controller.dtos.EditAccountRequest;
import de.stammtisch.pokerstats.controller.dtos.RegisterRequest;
import de.stammtisch.pokerstats.models.Role;
import de.stammtisch.pokerstats.models.User;
import de.stammtisch.pokerstats.repository.UserRepository;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.NoSuchElementException;
import java.util.Objects;

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
     * Extracts the token from the cookies.
     *
     * @param cookies The cookies to extract the token from.
     * @return The token or null if it does not exist.
     */
    public String getTokenFromCookie(String cookies) {
        if (cookies == null) {
            return null;
        }
        for (String cookie : cookies.split("; ")) {
            if (cookie.startsWith("Authorization=")) {
                return cookie.split("=")[1];
            }
        }
        return null;
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
     * @param token The JWT-Token to extract the user from.
     * @return The user from the token.
     * @throws NoSuchElementException If the user does not exist.
     * @throws IllegalArgumentException If the token is invalid.
     * @throws JwtException If the token is invalid.
     */
    public @NonNull User getUserFromToken(String token) {
        return this.userRepository.findByName(this.jwtService.extractUsernameFromToken(token)).orElseThrow();
    }



    /**
     * Generates a token for the given user.
     *
     * @param user The user to generate the token for.
     * @return The generated token.
     */
    public String generateToken(@NonNull User user) {
        return this.jwtService.generateToken(null, user);
    }

    public String register(@NonNull RegisterRequest request) throws IOException {
        if (this.userRepository.existsByName(request.name())) {
            throw new IllegalArgumentException("User %s already exists.".formatted(request.name()));
        }
        final User user = new User();
        user.setName(request.name());
        user.setPassword(this.passwordEncoder.encode(request.password()));
        user.setRole(Role.GUEST);
        user.setBuyIn(4);
        user.setProfilePictureType(
                Objects.requireNonNull(request.picture().getOriginalFilename()).split("\\.")[1]
        );

        final File onDisk = new File("%s/data/user/%s/picture.%s".formatted(
                System.getProperty("user.dir"),
                URLEncoder.encode(user.getUsername(), StandardCharsets.UTF_8),
                Objects.requireNonNull(request.picture().getOriginalFilename()).split("\\.")[1]
        ));
        Files.createDirectories(onDisk.getParentFile().toPath());
        request.picture().transferTo(onDisk);
        this.userRepository.save(user);
        return this.generateToken(user);
    }

    public String changeDetails(@NonNull EditAccountRequest request, String cookies, MultipartFile picture) throws IOException {
        String token = this.getTokenFromCookie(cookies);
        User user = this.getUserFromToken(token);
        if (!this.passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new IllegalArgumentException("Old password is incorrect.");
        }
        if (request.newPassword() != null) {
            user.setPassword(this.passwordEncoder.encode(request.newPassword()));
        }
        if (2 <= request.buyIn() && request.buyIn() <= 5) {
            user.setBuyIn(request.buyIn());
        } else {
            throw new IllegalArgumentException("Buy-in must be between 2 and 5.");
        }

        if (picture != null) {
            final File onDisk = new File("%s/data/user/%s/picture.%s".formatted(
                    System.getProperty("user.dir"),
                    URLEncoder.encode(user.getUsername(), StandardCharsets.UTF_8),
                    Objects.requireNonNull(picture.getOriginalFilename()).split("\\.")[1]
            ));
            Files.createDirectories(onDisk.getParentFile().toPath());
            picture.transferTo(onDisk);
            user.setProfilePictureType(
                    Objects.requireNonNull(picture.getOriginalFilename()).split("\\.")[1]
            );
        }
        user.setName(request.name());
        token = this.generateToken(user);
        this.userRepository.save(user);
        return token;
    }
}

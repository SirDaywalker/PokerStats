package de.stammtisch.pokerstats.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.NoSuchElementException;
import java.util.Objects;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import de.stammtisch.pokerstats.controller.dtos.AuthenticationRequest;
import de.stammtisch.pokerstats.controller.dtos.EditAccountRequest;
import de.stammtisch.pokerstats.controller.dtos.PasswordOrConfirmationRequest;
import de.stammtisch.pokerstats.controller.dtos.RegisterRequest;
import de.stammtisch.pokerstats.exceptions.ConfirmationTimeExceededException;
import de.stammtisch.pokerstats.exceptions.EmailAlreadyInUseException;
import de.stammtisch.pokerstats.exceptions.InvalidRequestParameterException;
import de.stammtisch.pokerstats.exceptions.UserAlreadyEnabledException;
import de.stammtisch.pokerstats.exceptions.UserNotEnabledException;
import de.stammtisch.pokerstats.models.Confirmation;
import de.stammtisch.pokerstats.models.Role;
import de.stammtisch.pokerstats.models.User;
import de.stammtisch.pokerstats.repository.ConfirmationRepository;
import de.stammtisch.pokerstats.repository.UserRepository;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.Cookie;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthenticationService {
    private final JWTService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final ConfirmationService confirmationService;
    private final ConfirmationRepository confirmationRepository;

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
            if (!cookie.startsWith("Authorization=")) {
                continue;
            }
            try {
                return cookie.split("=")[1];
            } catch (ArrayIndexOutOfBoundsException ignored) {
                return null;
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
            throw new InvalidRequestParameterException("Der Name ist bereits vergeben.");
        }
        if (emailIsNotValid(request.email()) || nameIsNotValid(request.name())) {
            throw new InvalidRequestParameterException("Der Name oder die E-Mail-Adresse ist ungültig.");
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyInUseException(request.email());
        }

        final User user = new User();
        user.setName(request.name());
        user.setPassword(this.passwordEncoder.encode(request.password()));
        user.setRole(Role.GUEST);
        user.setBuyIn(4);
        user.setProfilePictureType(
                Objects.requireNonNull(request.picture().getOriginalFilename()).split("\\.")[1]
        );
        user.setEmail(request.email());
        this.userRepository.save(user);
        
        final Confirmation confirmation = this.confirmationService.createConfirmation(user);
        this.confirmationService.sendConfirmationMail(user, confirmation);

        final File onDisk = new File("%s/data/user/%d/picture.%s".formatted(
                System.getProperty("user.dir"),
                user.getId(),
                Objects.requireNonNull(request.picture().getOriginalFilename()).split("\\.")[1]
        ));
        Files.createDirectories(onDisk.getParentFile().toPath());
        request.picture().transferTo(onDisk);
        return this.generateToken(user);
    }
    
    public String requestConfirmation(@NonNull PasswordOrConfirmationRequest request) {
    	final User user = this.userRepository.findByName(request.name()).orElseThrow();
    	if(user.isEnabled()) {
    		throw new UserAlreadyEnabledException();
    	}
    	final Confirmation confirmation = this.confirmationService.createConfirmation(user);
        this.confirmationService.sendConfirmationMail(user, confirmation);
        return this.generateToken(user);
    }
    
    public String requestPasswordReset(@NonNull PasswordOrConfirmationRequest request) {
    	final User user = this.userRepository.findByName(request.name()).orElseThrow();
    	if(!user.isEnabled()) {
    		throw new UserNotEnabledException();
    	}
    	return this.generateToken(user);
    }

    public String changeDetails(@NonNull EditAccountRequest request, String cookies) throws IOException {
        String token = this.getTokenFromCookie(cookies);
        final User account = this.getUserFromToken(token);

        long targetID = request.targetId();
        if (targetID != account.getId() && !account.getRole().equals(Role.ADMIN)) {
            throw new IllegalArgumentException("Du hast nicht die Berechtigung, diese Aktion auszuführen.");
        }

        final User user = this.userRepository.findById(targetID).orElseThrow();
        if (!this.passwordEncoder.matches(request.password(), account.getPassword())) {
            throw new BadCredentialsException("Das Passwort ist falsch.");
        }

        if (emailIsNotValid(request.email()) || nameIsNotValid(request.name())) {
            throw new IllegalArgumentException("Der Name oder die E-Mail-Adresse ist ungültig.");
        }

        if (request.role() != null) {
            user.setRole(Role.valueOf(request.role()));
        }

        if (request.newPassword() != null) {
            if (request.newPassword().isBlank()) {
                throw new IllegalArgumentException("Das neue Passwort darf nicht leer sein.");
            }
            user.setPassword(this.passwordEncoder.encode(request.newPassword()));
        }

        int buyIn;
        if (user.getRole().equals(Role.GUEST)) {
            buyIn = Math.min(5, Math.max(4, request.buyIn()));  // 4 is the default buy-in for guests
        } else {
            buyIn = Math.min(5, Math.max(2, request.buyIn()));  // 2 is the default buy-in for members
        }

        user.setBuyIn(buyIn);
        MultipartFile picture = request.picture();
        if (picture != null) {
            String type = picture.getContentType();
            if (type == null || !type.startsWith("image/")) {
                throw new IllegalArgumentException("Das Bild ist ungültig.");
            }
            type = type.split("/")[1];
            final File onDisk = new File("%s/data/user/%d/picture.%s".formatted(
                    System.getProperty("user.dir"),
                    user.getId(),
                    type
            ));
            Files.createDirectories(onDisk.getParentFile().toPath());
            picture.transferTo(onDisk);
            user.setProfilePictureType(type);
        }

        user.setName(request.name());
        if (userRepository.existsByEmail(request.email()) && !user.getEmail().equals(request.email())) {
            throw new EmailAlreadyInUseException(request.email());
        }
        user.setEmail(request.email());
        this.userRepository.save(user);

        if (account.getId() == user.getId()) {
            token = this.generateToken(user);
        } else {
            token = this.generateToken(account);
        }
        return token;
    }

    /**
     * Email must contain an @ symbol.
     * Email must contain a domain name.
     *
     * @param email The email to check.
     * @return True if the email is not valid, false otherwise.
     */
    private static boolean emailIsNotValid(String email) {
        if (email == null) {
            return true;
        }
        return !email.matches("^[\\w\\-.]+@([\\w-]+\\.)+[\\w-]{2,6}$");
    }

    /**
     * Name must not contain any digits.
     *
     * @param name The name to check.
     * @return True if the name is not valid, false otherwise.
     */
    private static boolean nameIsNotValid(String name) {
        if (name == null) {
            return true;
        }
        return name.matches(".*\\d.*");
    }
}

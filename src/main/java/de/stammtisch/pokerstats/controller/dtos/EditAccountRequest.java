package de.stammtisch.pokerstats.controller.dtos;

import org.springframework.web.multipart.MultipartFile;

public record EditAccountRequest (
        String name,
        String password,
        String newPassword,
        int buyIn,
        MultipartFile picture,
        String email,
        String role,
        String targetName
) {}

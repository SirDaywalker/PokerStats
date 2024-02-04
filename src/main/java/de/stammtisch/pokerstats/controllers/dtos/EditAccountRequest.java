package de.stammtisch.pokerstats.controllers.dtos;

import org.springframework.web.multipart.MultipartFile;

public record EditAccountRequest(
        String name,
        String oldPassword,
        String newPassword,
        double buyIn,
        MultipartFile picture
) {}

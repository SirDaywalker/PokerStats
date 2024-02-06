package de.stammtisch.pokerstats.controller.dtos;

import org.springframework.web.multipart.MultipartFile;

public record EditAccountRequest(
        String name,
        String oldPassword,
        String newPassword,
        double buyIn,
        MultipartFile picture
) {}

package de.stammtisch.pokerstats.controllers.dtos;

import org.springframework.web.multipart.MultipartFile;

public record RegisterRequest (
        String name,
        String password,
        MultipartFile picture
) {}

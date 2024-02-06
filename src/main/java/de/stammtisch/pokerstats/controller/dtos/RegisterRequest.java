package de.stammtisch.pokerstats.controller.dtos;

import org.springframework.web.multipart.MultipartFile;

public record RegisterRequest (
        String name,
        String password,
        MultipartFile picture
) {}

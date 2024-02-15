package de.stammtisch.pokerstats.controller.dtos;

public record EditAccountRequest(
        String name,
        String password,
        String newPassword,
        int buyIn
) {}

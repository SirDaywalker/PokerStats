package de.stammtisch.pokerstats.controller.dtos;

public record AuthenticationRequest(
        String name,
        String password
) {}

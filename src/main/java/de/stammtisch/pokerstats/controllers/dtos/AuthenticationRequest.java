package de.stammtisch.pokerstats.controllers.dtos;

public record AuthenticationRequest(
        String name,
        String password
) {}

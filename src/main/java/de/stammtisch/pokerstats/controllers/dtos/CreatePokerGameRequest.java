package de.stammtisch.pokerstats.controllers.dtos;

public record CreatePokerGameRequest(
    String[] players,
    String notes,
    String winner
) {}

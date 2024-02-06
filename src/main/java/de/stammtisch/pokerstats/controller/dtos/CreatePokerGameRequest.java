package de.stammtisch.pokerstats.controller.dtos;

public record CreatePokerGameRequest(
    String[] players,
    String notes,
    String winner
) {}

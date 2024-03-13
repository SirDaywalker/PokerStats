package de.stammtisch.pokerstats.controller.dtos;

public record UpdatePokerGameRequest (
    Long gameId,
    Long userId
) {}

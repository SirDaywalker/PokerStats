package de.stammtisch.pokerstats.controller.dtos;

public record UpdatePokerGameRequest (
    Long gameId,
    long[] playerIds,
    long winnerId
) {}

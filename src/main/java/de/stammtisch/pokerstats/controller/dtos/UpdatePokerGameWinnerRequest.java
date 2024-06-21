package de.stammtisch.pokerstats.controller.dtos;

public record UpdatePokerGameWinnerRequest(
    Long gameId,
    long winnerId
) {}

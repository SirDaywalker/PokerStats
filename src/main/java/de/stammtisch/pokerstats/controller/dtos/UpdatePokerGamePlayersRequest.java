package de.stammtisch.pokerstats.controller.dtos;

public record UpdatePokerGamePlayersRequest (
    long gameId,
    long[] playerIds
){}

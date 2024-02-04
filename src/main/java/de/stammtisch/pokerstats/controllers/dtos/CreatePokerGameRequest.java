package de.stammtisch.pokerstats.controllers.dtos;

import java.util.ArrayList;

public record CreatePokerGameRequest(
    ArrayList<String> players,
    String notes,
    String winner
) {}

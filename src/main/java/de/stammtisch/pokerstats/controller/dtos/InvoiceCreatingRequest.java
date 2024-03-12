package de.stammtisch.pokerstats.controller.dtos;

public record InvoiceCreatingRequest(
    String title,
    Double amount,
    Long due,
    Double interest,
    Integer interestIntervalWeeks,
    String[] users
) {}

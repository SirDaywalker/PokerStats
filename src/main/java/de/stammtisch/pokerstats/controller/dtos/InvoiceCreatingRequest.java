package de.stammtisch.pokerstats.controller.dtos;

import java.util.Date;

public record InvoiceCreatingRequest(
    String title,
    Double amount,
    Date due,
    Double interest,
    Integer interestIntervalDays,
    String[] users
) {}

package de.stammtisch.pokerstats.exceptions;

public class ConfirmationTimeExceededException extends RuntimeException{
	public ConfirmationTimeExceededException() {
        super("Confirmation exceeded its 15min time window.");
    }
}

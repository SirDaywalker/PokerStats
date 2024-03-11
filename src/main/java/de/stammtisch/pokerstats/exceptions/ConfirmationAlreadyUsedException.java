package de.stammtisch.pokerstats.exceptions;

public class ConfirmationAlreadyUsedException extends RuntimeException{
	public ConfirmationAlreadyUsedException() {
		super("Token of Confirmation has already been validated.");
	}
}

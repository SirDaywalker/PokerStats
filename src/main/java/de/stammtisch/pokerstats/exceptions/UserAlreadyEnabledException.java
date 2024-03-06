package de.stammtisch.pokerstats.exceptions;

public class UserAlreadyEnabledException extends RuntimeException{
	public UserAlreadyEnabledException() {
		super("User's email is already confirmed and user is enabled.");
	}
}

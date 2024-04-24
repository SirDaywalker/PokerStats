package de.stammtisch.pokerstats.exceptions;

public class UserNotEnabledException extends RuntimeException{
	public UserNotEnabledException() {
		super("User has not been enabled.");
	}
}

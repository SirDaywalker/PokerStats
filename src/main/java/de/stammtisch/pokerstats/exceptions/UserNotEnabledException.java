package de.stammtisch.pokerstats.exceptions;

public class UserNotEnabledException extends RuntimeException{
	public UserNotEnabledException() {
		super("Benutzer wurde noch nicht bestätigt!");
	}
}

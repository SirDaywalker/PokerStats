package de.stammtisch.pokerstats.exceptions;

public class UnautherizedUserException extends Exception {
	public UnautherizedUserException() {
		super("User unautherized");
	}
}

package de.stammtisch.pokerstats.exceptions;

public class EmailAlreadyInUseException extends RuntimeException {
    public EmailAlreadyInUseException(String email) {
        super("Email %s is already in use".formatted(email));
    }
}

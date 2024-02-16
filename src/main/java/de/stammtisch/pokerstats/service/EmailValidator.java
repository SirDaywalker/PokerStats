package de.stammtisch.pokerstats.service;

import java.util.regex.Pattern;

public class EmailValidator {
	
	private static Pattern pattern = Pattern.compile("^(?=.{1,64}@)[A-Za-z0-9_-]+"
									+ "(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+"
									+ "(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$");
	
	public static boolean checkEmail(String email) {
		if(email.isEmpty() || email == null) return false;
		
		return pattern.matcher(email).matches() ? true : false;
	}
	
}

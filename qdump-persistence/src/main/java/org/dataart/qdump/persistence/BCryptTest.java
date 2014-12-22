package org.dataart.qdump.persistence;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class BCryptTest {
	public static void main(String[] args) {
		String password = "password";
		String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
		System.out.println(BCrypt.checkpw(password, hashedPassword));
		String doubleHashedPassword = BCrypt.hashpw(hashedPassword, BCrypt.gensalt(12));
		System.out.println(BCrypt.checkpw(password, doubleHashedPassword));
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		String encoderedPassword = encoder.encode(password);
		System.out.println(encoder.matches(password, encoderedPassword));
	}
}

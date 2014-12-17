package org.dataart.qdump.persistence;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BCryptTest {
	public static void main(String[] args) {
		/*String password = "password";
		String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
		System.out.println(BCrypt.checkpw(password, hashedPassword));
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		String encoderedPassword = encoder.encode(password);
		System.out.println(encoder.matches(password, encoderedPassword));*/
		DateFormat format = new SimpleDateFormat("MMM d y");
		System.out.println(format.format(new Date()));
		System.out.println(new Date());
	}
}

package lk.parinda.springsecu.service;

import java.util.Base64;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;

public class EncodeAndDecode {

	public String get64EncodedString(String text) {
		return Base64.getEncoder().encodeToString(text.getBytes());
	}
	
	public String get64DecodedString(String text) {
		return new String(Base64.getDecoder().decode(text));
	}
	
	public String getBCryptString(String text) {
		return BCrypt.hashpw(text, BCrypt.gensalt(16));
	}
	
	public String getSCryptString(String text) {
		SCryptPasswordEncoder scr = new SCryptPasswordEncoder();
		return scr.encode(text);
	}
	
	public boolean checkBCryptString(String hasedText,String normalText) {
		return BCrypt.checkpw(normalText, hasedText);
	}
	
}

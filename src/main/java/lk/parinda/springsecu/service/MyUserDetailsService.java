package lk.parinda.springsecu.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//EncodeAndDecode endoder = new EncodeAndDecode();
		Map<String, String> info = new HashMap<>();
		info.put("pary", "1234");
		info.put("gayan", "7896");
		if(info.containsKey(username)) {
			List<SimpleGrantedAuthority> role =  Arrays.stream("ROLE_USER".split(",")).map(SimpleGrantedAuthority :: new).collect(Collectors.toList());
			//System.out.print(endoder.getBCryptString(info.get(username)));
			//return new User(username, endoder.getBCryptString(info.get(username)), role);
			//String text = endoder.getSCryptString(info.get(username));
			//System.out.print(text);
			//return new User(username, text, role);
			
			return new User(username, info.get(username), role);
		}
		return new User("", "", new ArrayList<>());
	}

}

package lk.parinda.springsecu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import lk.parinda.springsecu.model.AuthonticationRequest;
import lk.parinda.springsecu.model.AuthonticationResponse;
import lk.parinda.springsecu.model.ErrorRes;
import lk.parinda.springsecu.service.MyUserDetailsService;
import lk.parinda.springsecu.staticData.StaticData;
import lk.parinda.springsecu.unil.JwtUtil;

@RestController
public class HelloRes {
	
	@Autowired
	private AuthenticationManager authManager;
	
	@Autowired
	private MyUserDetailsService userDetailsService;
	
	@Autowired
	private JwtUtil jwtTokenUtill;
	
	@Autowired
	private StaticData data;
	
	
	@RequestMapping(method = RequestMethod.GET, value = "/user")
	public String HelloWorld() {
		return "HelloWorldaaaa";
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/atho")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthonticationRequest authenticationRequest) throws Exception{
		try {
			authManager.authenticate(
					new UsernamePasswordAuthenticationToken(
							authenticationRequest.getUserName(), authenticationRequest.getPassWord()
							)
					);
			final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUserName());
			final String jwt = data.getAccessTokenHead()+jwtTokenUtill.generateToken(userDetails);
			final String refresh_jwt = data.getRefreshTokenHead()+jwtTokenUtill.generateRefreshToken(userDetails);
			
			return ResponseEntity.ok(new AuthonticationResponse(jwt,refresh_jwt));
		}catch (BadCredentialsException e) {
			System.out.print("case numbe"+e.getMessage());
			return ResponseEntity.ok(new ErrorRes(e.getMessage()+"  Username not Found",4001));
		}catch(AuthenticationException e) {
			System.out.print("case numbe"+e.getMessage());
			return ResponseEntity.ok(new ErrorRes(e.getMessage()+"  Username not Found",4002));
		}catch(Exception e) {
			System.out.print("case numbe"+e.getMessage());
			return ResponseEntity.badRequest().body(new ErrorRes(e.getMessage()+"  Some Error",4003));
		}
		
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/refresh")
	public ResponseEntity<?> RefreshAuthenticationToken(@RequestHeader("AuToken") String token ) throws Exception{
		
		if(token != null && token.startsWith(data.getRefreshTokenHead())) {
			String refresh_jwt = token.substring(7);
			String username = jwtTokenUtill.extractUsername(refresh_jwt);
			if(username != null) {
				
				final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
				
				if (jwtTokenUtill.validateToken(refresh_jwt, userDetails)) {
					try {
						final String jwt = data.getAccessTokenHead()+jwtTokenUtill.generateToken(userDetails);
						//final String new_refresh_jwt = data.getRefreshTokenHead()+jwtTokenUtill.generateRefreshToken(userDetails);
						return ResponseEntity.ok(new AuthonticationResponse(jwt,jwt));
					}catch(Exception e) {
						System.out.print("case numbe"+e.getMessage());
						return ResponseEntity.badRequest().body(new ErrorRes(e.getMessage()+"  Some Error. Please logout",4004));
					}
				}else {
					return ResponseEntity.ok(new ErrorRes("Epierded Token. Please logout",4005));
				}
			}else {
				return ResponseEntity.badRequest().body(new ErrorRes("Incorect Token. Please logout",4006));
			}
		}else {
			return ResponseEntity.badRequest().body(new ErrorRes("Incorect Token. Please logout",4007));
		}
		
	}

}

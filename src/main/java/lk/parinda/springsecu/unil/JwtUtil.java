package lk.parinda.springsecu.unil;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtUtil {
	
	private String SECRET_KEY = "test_2020";

    public Map<String,Object> extractUsername(String token) {
    	Claims temp = extractAllClaims(token);
    	if(temp.containsKey("MyCustomError")) {
    		return temp;
    	}else {
    		return Map.of("data", extractClaim(token, Claims::getSubject));
    	}
    }

    public Map<String,Object> extractExpiration(String token) {
    	Claims temp = extractAllClaims(token);
    	if(temp.containsKey("MyCustomError")) {
    		return temp;
    	}else {
    		return Map.of("data", extractClaim(token, Claims::getExpiration));
    	}
    }
    
    public Map<String,Object> getId(String token) {
    	Claims temp = extractAllClaims(token);
    	if(temp.containsKey("MyCustomError")) {
    		return temp;
    	}else {
    		return Map.of("data", extractClaim(token, Claims::getId)); /**  :: mean => make object from Claims class and call getId method **/
    	}
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public <T> T extractClaim(Claims claims, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(claims);
    }

	private Claims extractAllClaims(String token) {
    	try {
    		return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    	}catch(ExpiredJwtException e) {
    		return Jwts.claims(Map.of("data", "Token Exp","MyCustomError",true));
    	}catch(Exception e) {
    		return Jwts.claims(Map.of("data", e.getMessage(),"MyCustomError",true));
    	}
    }

	public Boolean isTokenExpired(String token) {
		Map<String, Object> dataSet = extractExpiration(token);
		if(dataSet.containsKey("MyCustomError")) {
			return true;
		}else {
			return (((Date) dataSet.get("data")).before(new Date()));
		}
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername(),(1000 * 60 )); // valid only 1h
    }
    
    public String generateRefreshToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername(),(1000 * 60 * 60* 24* 4)); // valid only 4 days
    }

    private String createToken(Map<String, Object> claims, String subject,int expTime) {

        return Jwts.builder().setClaims(claims).setSubject(subject).setId("1").setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expTime))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
    	Map<String, Object> dataSet = extractUsername(token);
		if(dataSet.containsKey("MyCustomError")) {
			return false;
		}else {
	        return (((String)dataSet.get("data")).equals(userDetails.getUsername()) && !isTokenExpired(token));
		}
    }

}

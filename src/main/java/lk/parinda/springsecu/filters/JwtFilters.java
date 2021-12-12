package lk.parinda.springsecu.filters;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.JwtException;
import lk.parinda.springsecu.service.MyUserDetailsService;
import lk.parinda.springsecu.staticData.StaticData;
import lk.parinda.springsecu.unil.JwtUtil;

@Component
public class JwtFilters extends OncePerRequestFilter {
	
	@Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
	private StaticData data;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException, JwtException {
		final String authorizationHeader = request.getHeader("AuToken");
		
		String username = null;
        String jwt = null;

//        try {
	        if (authorizationHeader != null && authorizationHeader.startsWith(data.getAccessTokenHead())) {
	            jwt = authorizationHeader.substring(7);
	            
	            Map<String, Object> dayaSet = jwtUtil.extractUsername(jwt);
	            
	            if(dayaSet.containsKey("MyCustomError")) {
	            	ObjectMapper mapper = new ObjectMapper();
	            	Map<String, Object> errorDetails = new HashMap<>();
	            	errorDetails.put("massage", dayaSet.get("data"));
	            	if(dayaSet.get("data").toString().equals("Token Exp")) {
	            		errorDetails.put("statusCode", 50032);
	            	}else if(dayaSet.get("data").toString().contains("JWT validity")) {
	            		errorDetails.put("statusCode", 50033);
	            	}else {
	            		errorDetails.put("statusCode", 50034);
	            	}
	                errorDetails.put("dataSet", null);
	                errorDetails.put("error", true);
	                response.setStatus(HttpStatus.FORBIDDEN.value());
	                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
	                mapper.writeValue(response.getWriter(), errorDetails);
	            }else {
	            	username = (String) jwtUtil.extractUsername(jwt).get("data");
	            	if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
	    	            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
	    	            if (jwtUtil.validateToken(jwt, userDetails)) {
	    	                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
	    	                        userDetails, null, userDetails.getAuthorities());
	    	                usernamePasswordAuthenticationToken
	    	                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
	    	                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
	    	            }
	    	        }
	            }
	            
//	            if(jwtUtil.isTokenExpired(jwt)) {
//	            	ObjectMapper mapper = new ObjectMapper();
//	            	Map<String, Object> errorDetails = new HashMap<>();
//	            	errorDetails.put("massage", "Token expired");
//	                errorDetails.put("statusCode", 50032);
//	                errorDetails.put("dataSet", null);
//	                errorDetails.put("error", true);
//	                response.setStatus(HttpStatus.FORBIDDEN.value());
//	                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//	                mapper.writeValue(response.getWriter(), errorDetails);
//	            }else {
//	            	username = (String) jwtUtil.extractUsername(jwt).get("data");
//	            	if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//	    	            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
//	    	            if (jwtUtil.validateToken(jwt, userDetails)) {
//	    	                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
//	    	                        userDetails, null, userDetails.getAuthorities());
//	    	                usernamePasswordAuthenticationToken
//	    	                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//	    	                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
//	    	            }
//	    	        }
//	            }
	        }
//        }catch(Exception e) {
//        	ObjectMapper mapper = new ObjectMapper();
//        	Map<String, Object> errorDetails = new HashMap<>();
//        	errorDetails.put("massage", e.getMessage());
//            errorDetails.put("statusCode", 50032);
//            errorDetails.put("dataSet", null);
//            errorDetails.put("error", true);
//            response.setStatus(HttpStatus.FORBIDDEN.value());
//            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//            mapper.writeValue(response.getWriter(), errorDetails);
//        }
        
//        filterChain.doFilter(request, response);
        
        try {
        	filterChain.doFilter(request, response);
        }catch(Exception e) {
//        	System.err.println(e.getMessage()+"chaine");
        }
	}

}

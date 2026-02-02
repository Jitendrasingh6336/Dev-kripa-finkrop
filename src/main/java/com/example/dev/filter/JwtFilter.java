package com.example.dev.filter;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.dev.util.JwtUtil;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tools.jackson.databind.ObjectMapper;

@Component
public class JwtFilter extends OncePerRequestFilter{
	
	 private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);
	    
	    private final JwtUtil jwtUtil;
	    private final UserDetailsService userDetailsService;
	    private final ObjectMapper objectMapper;
	    
	    public JwtFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
	        this.jwtUtil = jwtUtil;
	        this.userDetailsService = userDetailsService;
	        this.objectMapper = new ObjectMapper();
	    }

	    @Override
	    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
	            throws ServletException, IOException {

	        final String authHeader = request.getHeader("Authorization");
	        String email = null;
	        String jwt = null;

	        try {
	            if (authHeader != null && authHeader.startsWith("Bearer ")) {
	                jwt = authHeader.substring(7);
	                email = jwtUtil.extractUsername(jwt);
	            }

	            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
	                UserDetails userDetails = this.userDetailsService.loadUserByUsername(email);
	                if (jwtUtil.validateToken(jwt, userDetails.getUsername())) {
	                    UsernamePasswordAuthenticationToken authToken =
	                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
	                    SecurityContextHolder.getContext().setAuthentication(authToken);
	                }
	            }

	            filterChain.doFilter(request, response);
	            
	        } catch (ExpiredJwtException e) {
	            logger.error("JWT token is expired: {}", e.getMessage());
	            handleException(response, HttpStatus.UNAUTHORIZED, "JWT token is expired", "EXPIRED_TOKEN");
	        } catch (UnsupportedJwtException e) {
	            logger.error("JWT token is unsupported: {}", e.getMessage());
	            handleException(response, HttpStatus.UNAUTHORIZED, "JWT token is unsupported", "UNSUPPORTED_TOKEN");
	        } catch (MalformedJwtException e) {
	            logger.error("JWT token is malformed: {}", e.getMessage());
	            handleException(response, HttpStatus.UNAUTHORIZED, "JWT token is malformed", "MALFORMED_TOKEN");
	        } catch (JwtException e) {
	            logger.error("JWT token validation failed: {}", e.getMessage());
	            handleException(response, HttpStatus.UNAUTHORIZED, "JWT token validation failed", "INVALID_TOKEN");
	        } catch (UsernameNotFoundException e) {
	            logger.error("User not found: {}", e.getMessage());
	            handleException(response, HttpStatus.UNAUTHORIZED, "User not found", "USER_NOT_FOUND");
	        } catch (Exception e) {
	            logger.error("Authentication error: {}", e.getMessage());
	            handleException(response, HttpStatus.INTERNAL_SERVER_ERROR, "Authentication error", "AUTHENTICATION_ERROR");
	        }
	    }

	    private void handleException(HttpServletResponse response, HttpStatus status, String message, String errorCode) 
	            throws IOException {
	        response.setStatus(status.value());
	        response.setContentType("application/json");
	        response.setCharacterEncoding("UTF-8");

	        ErrorResponse errorResponse = new ErrorResponse(
	            status.value(),
	            status.getReasonPhrase(),
	            message,
	            errorCode,
	            System.currentTimeMillis()
	        );

	        String jsonResponse = objectMapper.writeValueAsString(errorResponse);
	        response.getWriter().write(jsonResponse);
	    }

	    // Inner class for error response structure
	    private static class ErrorResponse {
	        private int status;
	        private String error;
	        private String message;
	        private String errorCode;
	        private long timestamp;

	        public ErrorResponse(int status, String error, String message, String errorCode, long timestamp) {
	            this.status = status;
	            this.error = error;
	            this.message = message;
	            this.errorCode = errorCode;
	            this.timestamp = timestamp;
	        }

	        // Getters
	        public int getStatus() { return status; }
	        public String getError() { return error; }
	        public String getMessage() { return message; }
	        public String getErrorCode() { return errorCode; }
	        public long getTimestamp() { return timestamp; }
	    }

}

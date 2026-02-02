package com.example.dev.config;

import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tools.jackson.databind.ObjectMapper;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

	 private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);
	    private final ObjectMapper objectMapper = new ObjectMapper();

	    @Override
	    public void commence(HttpServletRequest request, HttpServletResponse response,
	                         AuthenticationException authException) throws IOException, ServletException {
	        
	        logger.error("Unauthorized access attempt: {}", authException.getMessage());
	        
	        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	        response.setContentType("application/json");
	        response.setCharacterEncoding("UTF-8");

	        ErrorResponse errorResponse = new ErrorResponse(
	            HttpServletResponse.SC_UNAUTHORIZED,
	            "Unauthorized",
	            "Access denied. Please provide valid authentication credentials.",
	            "UNAUTHORIZED_ACCESS",
	            System.currentTimeMillis()
	        );

	        String jsonResponse = objectMapper.writeValueAsString(errorResponse);
	        response.getWriter().write(jsonResponse);
	    }

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

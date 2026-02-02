package com.example.dev.handers;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.dev.exception.ErrorResponse;
import com.example.dev.exception.ResourceAlreadyExistException;
import com.example.dev.exception.ResourceNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorResponse> resourceNotFoundException(ResourceNotFoundException rnfe) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body((ErrorResponse.builder().statusCode(HttpStatus.NOT_FOUND.value()).status(HttpStatus.NOT_FOUND)
						.message(rnfe.getMessage()).date(LocalDateTime.now()).build()));
	}
	
	@ExceptionHandler(ResourceAlreadyExistException.class)
	public ResponseEntity<ErrorResponse> resourceAlreadyExistException(ResourceAlreadyExistException raee) {
		return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
				.body((ErrorResponse.builder().statusCode(HttpStatus.NOT_ACCEPTABLE.value())
						.status(HttpStatus.NOT_ACCEPTABLE).message(raee.getMessage()).date(LocalDateTime.now())
						.build()));
	}


}

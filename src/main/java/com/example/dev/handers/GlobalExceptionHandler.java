package com.example.dev.handers;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MethodArgumentNotValidException;
import com.example.dev.exception.BadRequestException;
import com.example.dev.exception.ErrorResponse;
import com.example.dev.exception.ResourceAlreadyExistException;
import com.example.dev.exception.ResourceNotFoundException;
import com.example.dev.exception.SomethingWentWrongException;
import com.example.dev.exception.UnauthorizedException;
import com.example.dev.util.Constants;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import tools.jackson.databind.exc.InvalidFormatException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

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

	/**
	 * Handles UnauthorizationException and returns a 401 Unauthorized response.
	 *
	 * @param ue the UnauthorizationException
	 * @return ResponseEntity with error details
	 */
	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<ErrorResponse> unauthorizationException(UnauthorizedException ue) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
				.body((ErrorResponse.builder().statusCode(HttpStatus.UNAUTHORIZED.value())
						.status(HttpStatus.UNAUTHORIZED).message(ue.getMessage()).date(LocalDateTime.now()).build()));
	}

	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<ErrorResponse> badRequestException(BadRequestException ue) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body((ErrorResponse.builder().statusCode(HttpStatus.BAD_REQUEST.value()).status(HttpStatus.BAD_REQUEST)
						.message(ue.getMessage()).date(LocalDateTime.now()).build()));
	}

	/**
	 * Handles MethodArgumentNotValidException and returns a 400 Bad Request
	 * response with validation errors.
	 *
	 * @param ex the MethodArgumentNotValidException
	 * @return ResponseEntity with error details
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {

	    String msg = ex.getBindingResult()
	                   .getFieldError()
	                   .getDefaultMessage();

	    return ResponseEntity.badRequest().body(
	            ErrorResponse.builder()
	                    .statusCode(HttpStatus.BAD_REQUEST.value())
	                    .status(HttpStatus.BAD_REQUEST)
	                    .message(msg)
	                    .date(LocalDateTime.now())
	                    .build()
	    );
	}


	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException ex) {
		Map<String, String> errors = new HashMap<>();
		ErrorResponse response = ErrorResponse.builder().build();
		for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
//			String key = violation.getPropertyPath().toString();
//			String fieldName = key.contains(".") ? key.substring(key.lastIndexOf('.') + 1) : key;
			response.setMessage(violation.getMessage());
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}

	@ExceptionHandler(InvalidFormatException.class)
	public ResponseEntity<ErrorResponse> handleInvalidFormatException(InvalidFormatException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(ErrorResponse.builder().statusCode(HttpStatus.BAD_REQUEST.value()).status(HttpStatus.BAD_REQUEST)
						.message(ex.getOriginalMessage()).date(LocalDateTime.now()).build());
	}

	@ExceptionHandler(ExpiredJwtException.class)
	public ResponseEntity<ErrorResponse> handleExpiredJwtException(ExpiredJwtException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body((ErrorResponse.builder().statusCode(HttpStatus.BAD_REQUEST.value()).status(HttpStatus.BAD_REQUEST)
						.message(Constants.TOKEN_EXPIRED).date(LocalDateTime.now()).build()));
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorResponse> illegalArgumentException(IllegalArgumentException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body((ErrorResponse.builder().statusCode(HttpStatus.BAD_REQUEST.value()).status(HttpStatus.BAD_REQUEST)
						.message(ex.getMessage()).date(LocalDateTime.now()).build()));
	}

	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<ErrorResponse> handleMissingParams(MissingServletRequestParameterException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body((ErrorResponse.builder().statusCode(HttpStatus.BAD_REQUEST.value()).status(HttpStatus.BAD_REQUEST)
						.message(ex.getParameterName() + " is required").date(LocalDateTime.now()).build()));
	}

	@ExceptionHandler(MissingPathVariableException.class)
	public ResponseEntity<Map<String, String>> handleMissingPathVariable(MissingPathVariableException ex) {
		Map<String, String> response = new HashMap<>();
		String variableName = ex.getVariableName();
		response.put("error", "Required path variable '" + variableName + "' is missing");
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<Map<String, Object>> handleHttpRequestMethodNotSupported(
			HttpRequestMethodNotSupportedException ex) {
		Map<String, Object> response = new HashMap<>();
		response.put("error", "Method Not Allowed");
		response.put("message", "Request method " + ex.getMethod() + " is not supported.");
		response.put("status", HttpStatus.METHOD_NOT_ALLOWED.value());
		return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(response);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<Map<String, String>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
		Map<String, String> response = new HashMap<>();
		response.put(Constants.ERROR, "Required request body is missing");
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public ResponseEntity<ErrorResponse> handleMaxSizeException(MaxUploadSizeExceededException ex) {
		ErrorResponse errorResponse = ErrorResponse.builder().statusCode(HttpStatus.PAYLOAD_TOO_LARGE.value())
				.message("File size exceeds the maximum allowed limit!").date(LocalDateTime.now()).build();

		return new ResponseEntity<>(errorResponse, HttpStatus.PAYLOAD_TOO_LARGE);
	}

	@ExceptionHandler(SomethingWentWrongException.class)
	public ResponseEntity<ErrorResponse> somethingWentWrongException(SomethingWentWrongException rnfe) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body((ErrorResponse.builder().statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
						.status(HttpStatus.INTERNAL_SERVER_ERROR).message(rnfe.getMessage()).date(LocalDateTime.now())
						.build()));
	}
}

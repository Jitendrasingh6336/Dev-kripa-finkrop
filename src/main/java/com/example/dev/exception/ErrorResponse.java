package com.example.dev.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

	private String message;
	private HttpStatus status;
	private Integer statusCode;
	private LocalDateTime date;
	private Object response;

	public ErrorResponse(Integer statusCode, HttpStatus status, String message) {
		this.message = message;
		this.status = status;
		this.statusCode = statusCode;
	}
}


package com.example.dev.exception;

import com.example.dev.util.Constants;

public class BadRequestException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BadRequestException() {	
		super(Constants.BAD_REQUEST);
	}

	public BadRequestException(String message) {
		super(message);
	}
}

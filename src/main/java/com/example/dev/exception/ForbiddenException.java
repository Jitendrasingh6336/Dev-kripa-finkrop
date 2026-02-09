package com.example.dev.exception;

import com.example.dev.util.Constants;

public class ForbiddenException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ForbiddenException() {
		super(Constants.UNAUTHORIZED_ACCESS);
	}

	public ForbiddenException(String message) {
		super(message);
	}
}
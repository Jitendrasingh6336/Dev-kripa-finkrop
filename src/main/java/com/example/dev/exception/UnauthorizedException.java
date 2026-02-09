package com.example.dev.exception;

import com.example.dev.util.Constants;

public class UnauthorizedException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UnauthorizedException() {
		super(Constants.UNAUTHORIZED_ACCESS);
	}

	public UnauthorizedException(String message) {
		super(message);
	}

}

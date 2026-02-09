package com.example.dev.exception;

import com.example.dev.util.Constants;

public class SomethingWentWrongException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SomethingWentWrongException() {
		super(Constants.SOMETHING_WENT_WRONG);
	}

	public SomethingWentWrongException(String message) {
		super(message);
	}
}
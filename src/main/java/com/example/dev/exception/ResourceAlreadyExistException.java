package com.example.dev.exception;

public class ResourceAlreadyExistException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ResourceAlreadyExistException() {
		super();
	}

	public ResourceAlreadyExistException(String message) {
		super(message);
	}
}
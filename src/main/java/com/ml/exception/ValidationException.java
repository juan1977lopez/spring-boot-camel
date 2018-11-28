package com.ml.exception;

public class ValidationException extends Exception {
	
	private static final long serialVersionUID = 1L;
	private String message;
	
	public ValidationException(String message) {
		this.message = message;
	}
	
	@Override
	public String getMessage() {
		return message;
	}
}

package com.adosar.backend.business.exception;

public class InvalidPathVariableException extends Exception {
	public InvalidPathVariableException(String pathVariable) {
		super(String.format("variable %1$s is not valid in the current context", pathVariable));
	}
}

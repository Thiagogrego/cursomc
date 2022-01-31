package com.thiagogrego.cursomc.services.exceptions;

@SuppressWarnings("serial")
public class DataIntegrityException extends RuntimeException {

	public DataIntegrityException(String msg) {
		super(msg);
	}
	
	public DataIntegrityException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
	
}

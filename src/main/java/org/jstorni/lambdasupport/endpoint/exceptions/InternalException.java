package org.jstorni.lambdasupport.endpoint.exceptions;

public class InternalException extends RuntimeException {
	private final static String PREFIX = "INTERNAL :: ";

	public InternalException(String message) {
		super(PREFIX + message);
	}

	public InternalException(String message, Throwable cause) {
		super(PREFIX + message, cause);
	}

}
package com.emergya.openfleetservices.importer.exception;

public class NotYetImplementedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public NotYetImplementedException() {
	}

	public NotYetImplementedException(String message) {
		super(message);
	}

	public NotYetImplementedException(Throwable cause) {
		super(cause);
	}

	public NotYetImplementedException(String message, Throwable cause) {
		super(message, cause);
	}

}

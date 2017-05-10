package com.github.linggify.attic.exceptions;

/**
 * AtticRuntimeExceptions are used by Attic to signal significant unexpected errors at Runtime
 * @author Fredie
 *
 */
public class AtticRuntimeException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8284052304123883321L;
	
	/**
	 * Creates a new {@link AtticRuntimeException} with the given message
	 * @param message
	 */
	public AtticRuntimeException(String message) {
		super(message);
	}
}

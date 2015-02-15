package com.eric.ndim.domain.exception;

/**
 * @author Eric Vlaanderen
 */
public class DuplicateDBException extends Exception
{
	public DuplicateDBException(final String message)
	{
		super(message);
	}
}

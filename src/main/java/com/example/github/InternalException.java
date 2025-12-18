package com.example.github;

/**
 * Exception thrown when an internal server error occurs.
 */
public class InternalException extends RuntimeException {
    /**
     * Constructs a new InternalException with the specified message.
     *
     * @param message the error message
     */
    public InternalException(final String message) {
        super(message);
    }

    /**
     * Constructs a new InternalException with the specified message and cause.
     *
     * @param message the error message
     * @param cause the cause of the exception
     */
    public InternalException(final String message, final Throwable cause) {
        super(message, cause);
    }
}

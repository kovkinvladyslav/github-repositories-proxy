package com.example.github;

/**
 * Exception thrown when a GitHub user is not found.
 */
public final class UserNotFoundException extends RuntimeException {
    UserNotFoundException(final String username) {
        super("User not found: " + username);
    }
}

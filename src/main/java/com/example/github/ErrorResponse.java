package com.example.github;

/**
 * Error response containing status code and message.
 *
 * @param status HTTP status code
 * @param message error message
 */
public record ErrorResponse(int status, String message) { }

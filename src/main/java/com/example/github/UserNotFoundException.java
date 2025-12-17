package com.example.github;

public class UserNotFoundException extends RuntimeException{
    UserNotFoundException(String username) {
        super("User not found: " + username);
    }
}

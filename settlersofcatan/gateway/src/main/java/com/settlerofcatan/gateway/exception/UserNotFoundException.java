package com.settlerofcatan.gateway.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String message) {
        super("User not found in database with email = " + message);
    }

    public UserNotFoundException(Long id) {
        super("User not found in database with id = " + id);
    }
}

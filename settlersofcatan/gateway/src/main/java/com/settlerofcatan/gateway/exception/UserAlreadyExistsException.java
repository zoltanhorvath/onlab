package com.settlerofcatan.gateway.exception;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String email) {
        super("ApplicationUser with '" + email + "' email address already exists in DB.");
    }
}

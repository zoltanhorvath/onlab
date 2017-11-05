package com.settlerofcatan.gateway.exception;

public class RoleNotFoundException extends RuntimeException {

    public RoleNotFoundException(Long id) {
        super("RoleType not found with id = " + id);
    }
}

package com.settlerofcatan.gateway.exception;

public class RoleAlreadyExistsException extends RuntimeException {

    public RoleAlreadyExistsException(Long id) {
        super("RoleType already exists with id = " + id);
    }

    public RoleAlreadyExistsException(String name) {
        super("Role already exists with name = " + name);
    }

}

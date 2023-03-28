package com.ilario.sparkmart.exceptions.roles;

public class RoleNotFoundException extends Exception {
    public RoleNotFoundException(String errorMessage) {
        super(errorMessage);
    }
    public RoleNotFoundException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}

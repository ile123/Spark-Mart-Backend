package com.ilario.sparkmart.exceptions.users;

public class UserAuthenticationException extends Exception {
    public UserAuthenticationException(String errorMessage) { super(errorMessage); }
}

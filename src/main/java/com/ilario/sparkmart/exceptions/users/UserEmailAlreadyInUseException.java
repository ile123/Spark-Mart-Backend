package com.ilario.sparkmart.exceptions.users;

public class UserEmailAlreadyInUseException extends Exception{
    public UserEmailAlreadyInUseException(String errorMessage) {
        super(errorMessage);
    }
    public UserEmailAlreadyInUseException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}

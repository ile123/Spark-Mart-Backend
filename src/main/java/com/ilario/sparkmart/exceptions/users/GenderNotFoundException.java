package com.ilario.sparkmart.exceptions.users;

public class GenderNotFoundException extends Exception{
    public GenderNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}

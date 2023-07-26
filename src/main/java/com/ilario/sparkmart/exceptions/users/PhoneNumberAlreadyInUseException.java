package com.ilario.sparkmart.exceptions.users;

public class PhoneNumberAlreadyInUseException extends Exception{
    public PhoneNumberAlreadyInUseException(String errorMessage) { super(errorMessage); }
}

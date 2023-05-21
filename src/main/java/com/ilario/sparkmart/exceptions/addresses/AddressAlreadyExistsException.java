package com.ilario.sparkmart.exceptions.addresses;

public class AddressAlreadyExistsException extends Exception{
    public AddressAlreadyExistsException(String errorMessage) {
        super(errorMessage);
    }
    public AddressAlreadyExistsException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}

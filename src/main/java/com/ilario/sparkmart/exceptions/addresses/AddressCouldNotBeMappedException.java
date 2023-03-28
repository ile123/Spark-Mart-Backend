package com.ilario.sparkmart.exceptions.addresses;

public class AddressCouldNotBeMappedException extends Exception{
    public AddressCouldNotBeMappedException(String errorMessage) {
        super(errorMessage);
    }
    public AddressCouldNotBeMappedException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}

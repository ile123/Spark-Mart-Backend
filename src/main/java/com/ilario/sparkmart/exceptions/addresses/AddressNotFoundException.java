package com.ilario.sparkmart.exceptions.addresses;

public class AddressNotFoundException extends Exception{
    public AddressNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}

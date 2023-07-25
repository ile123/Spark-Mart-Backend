package com.ilario.sparkmart.exceptions.addresses;

public class AddressesNotFoundException extends Exception{
    public AddressesNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}

package com.ilario.sparkmart.exceptions.brands;

public class BrandsNotFoundException extends Exception{
    public BrandsNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}

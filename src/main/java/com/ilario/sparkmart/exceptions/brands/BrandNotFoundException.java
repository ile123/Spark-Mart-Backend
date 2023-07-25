package com.ilario.sparkmart.exceptions.brands;

public class BrandNotFoundException extends Exception{
    public BrandNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}

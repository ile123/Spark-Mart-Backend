package com.ilario.sparkmart.exceptions.products;

public class ProductNotFoundException extends Exception{
    public ProductNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}

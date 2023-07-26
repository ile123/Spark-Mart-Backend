package com.ilario.sparkmart.exceptions.orders;

public class OrderNotFoundException extends Exception{
    public OrderNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}

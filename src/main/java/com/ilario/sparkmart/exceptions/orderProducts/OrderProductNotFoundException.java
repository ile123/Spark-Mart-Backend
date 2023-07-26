package com.ilario.sparkmart.exceptions.orderProducts;

public class OrderProductNotFoundException extends Exception{
    public OrderProductNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}

package com.ilario.sparkmart.exceptions.orders;

public class OrdersNotFoundException extends Exception{
    public OrdersNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}

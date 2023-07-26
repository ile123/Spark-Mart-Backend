package com.ilario.sparkmart.exceptions.orderProducts;

public class OrderProductsNotFoundException extends Exception{
    public OrderProductsNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}

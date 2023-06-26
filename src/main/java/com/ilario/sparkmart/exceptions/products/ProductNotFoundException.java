package com.ilario.sparkmart.exceptions.products;

import com.ilario.sparkmart.models.Product;

public class ProductNotFoundException extends Exception{
    public ProductNotFoundException(String errorMessage) {
        super(errorMessage);
    }
    public ProductNotFoundException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}

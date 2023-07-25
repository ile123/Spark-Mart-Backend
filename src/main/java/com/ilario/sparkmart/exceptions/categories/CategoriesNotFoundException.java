package com.ilario.sparkmart.exceptions.categories;

public class CategoriesNotFoundException extends Exception {
    public CategoriesNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}

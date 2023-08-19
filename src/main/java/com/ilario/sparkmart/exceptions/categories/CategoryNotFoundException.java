package com.ilario.sparkmart.exceptions.categories;

public class CategoryNotFoundException extends Exception{
    public CategoryNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}

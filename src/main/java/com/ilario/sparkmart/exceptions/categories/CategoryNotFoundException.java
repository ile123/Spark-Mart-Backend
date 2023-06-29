package com.ilario.sparkmart.exceptions.categories;

import com.ilario.sparkmart.models.Category;

public class CategoryNotFoundException extends Exception{
    public CategoryNotFoundException(String errorMessage) {
        super(errorMessage);
    }
    public CategoryNotFoundException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}

package com.ilario.sparkmart.security.misc;

public record RegisterRequest(String firstName, String lastName, String phoneNumber, String email, String password, String gender, String role) {}

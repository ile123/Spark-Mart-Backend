package com.ilario.sparkmart.dto;

import java.util.UUID;

public record UserDTO(UUID id, UUID wishlistId,  String email,
                      String firstName, String lastName,
                      String phoneNumber, String gender, String role, UUID addressId) {
}
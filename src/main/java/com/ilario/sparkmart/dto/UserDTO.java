package com.ilario.sparkmart.dto;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

public record UserDTO(UUID id, UUID wishlistId,  String email,
                      String firstName, String lastName,
                      String phoneNumber, String role, UUID addressId) {
}
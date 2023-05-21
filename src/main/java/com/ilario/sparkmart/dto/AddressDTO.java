package com.ilario.sparkmart.dto;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;
public record AddressDTO(UUID id, String streetAddress, String city, String postalCode, String province, String country) {
}
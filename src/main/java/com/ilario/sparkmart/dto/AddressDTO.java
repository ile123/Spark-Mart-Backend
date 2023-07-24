package com.ilario.sparkmart.dto;

import java.util.UUID;
public record AddressDTO(UUID id, String streetAddress, String city, String postalCode, String province, String country) {
}
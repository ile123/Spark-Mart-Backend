package com.ilario.sparkmart.dto;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {
    @Nullable
    private UUID id;
    @Nullable
    private String streetAddress;
    @Nullable
    private String city;
    @Nullable
    private String postalCode;
    @Nullable
    private String province;
    @Nullable
    private String country;
}

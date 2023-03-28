package com.ilario.sparkmart.dto;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    @Nullable
    private UUID id;
    @Nullable
    private String username;
    @Nullable
    private String email;
    @Nullable
    private String firstName;
    @Nullable
    private String lastName;
    @Nullable
    private String phoneNumber;
    @Nullable
    private UUID roleId;
    @Nullable
    private UUID wishListId;
    @Nullable
    private UUID addressId;
}

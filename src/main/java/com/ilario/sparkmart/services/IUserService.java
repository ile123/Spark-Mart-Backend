package com.ilario.sparkmart.services;

import com.ilario.sparkmart.dto.AddressDTO;
import com.ilario.sparkmart.dto.UserDTO;

import java.util.List;
import java.util.UUID;

public interface IUserService {
    UserDTO getUserById(UUID id);
    List<UserDTO> getAllUsers();
    void saveUser(UserDTO userDTO);
    void updateUser(UUID userId, UserDTO userDTO);

    void updateAddress(UUID userId, AddressDTO addressDTO);

    void deleteUser(UUID userID);
}

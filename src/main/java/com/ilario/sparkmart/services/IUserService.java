package com.ilario.sparkmart.services;

import com.ilario.sparkmart.dto.AddressDTO;
import com.ilario.sparkmart.dto.UserDTO;
import com.ilario.sparkmart.models.User;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface IUserService {
    UserDTO getUserById(UUID id);
    UserDTO getUserByEmail(String email);
    Page<UserDTO> getAllUsers(int page, int pageSize);
    void updateUser(UUID userId, UserDTO userDTO);
    void updateAddress(UUID userId, AddressDTO addressDTO);

    void deleteUser(UUID userID);

    Boolean checkIfEmailIsAlreadyUsed(String email);

    void changeUserPassword(UUID userId, String newPassword);
}

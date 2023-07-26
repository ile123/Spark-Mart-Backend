package com.ilario.sparkmart.services;

import com.ilario.sparkmart.dto.AddressDTO;
import com.ilario.sparkmart.dto.UserDTO;
import com.ilario.sparkmart.exceptions.addresses.AddressCouldNotBeMappedException;
import com.ilario.sparkmart.exceptions.addresses.AddressNotFoundException;
import com.ilario.sparkmart.exceptions.users.UserEmailAlreadyInUseException;
import com.ilario.sparkmart.exceptions.users.UserNotFoundException;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface IUserService {
    UserDTO getById(UUID id) throws UserNotFoundException;
    void saveToDB(UserDTO userDTO) throws UserEmailAlreadyInUseException;
    void update(UUID id, UserDTO entity) throws UserNotFoundException;
    void delete(UUID id) throws UserNotFoundException;
    Page<UserDTO> getAll(int page, int pageSize, String sortBy, String sortDir, String userType, String keyword);
    UserDTO getUserByEmail(String email) throws UserNotFoundException;
    void updateAddress(UUID userId, AddressDTO addressDTO) throws UserNotFoundException, AddressCouldNotBeMappedException, AddressNotFoundException;
    Boolean checkIfEmailIsAlreadyUsed(String email);
    void changeUserPassword(UUID userId, String newPassword) throws UserNotFoundException;
    Boolean doesAdministratorExist();
}

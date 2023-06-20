package com.ilario.sparkmart.services;

import com.ilario.sparkmart.dto.AddressDTO;
import com.ilario.sparkmart.dto.UserDTO;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface IUserService extends IBaseService<UserDTO, UUID> {
    Page<UserDTO> getAll(int page, int pageSize, String sortBy, String sortDir, String userType, String keyword);
    UserDTO getUserByEmail(String email);
    void updateAddress(UUID userId, AddressDTO addressDTO);
    Boolean checkIfEmailIsAlreadyUsed(String email);

    void changeUserPassword(UUID userId, String newPassword);

    Boolean doesAdministratorExist();
}

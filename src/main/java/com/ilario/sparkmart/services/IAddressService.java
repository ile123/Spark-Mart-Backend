package com.ilario.sparkmart.services;

import com.ilario.sparkmart.dto.AddressDTO;
import com.ilario.sparkmart.dto.UserDTO;
import com.ilario.sparkmart.models.Address;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IAddressService {
    AddressDTO getById(UUID id);
    void saveToDB(AddressDTO addressDTO);
    Page<AddressDTO> getAll(int page, int pageSize, String sortBy, String sortDir, String keyword);
    Address getLastSavedAddress();
    Address getAddressByStreetNameAndCity(String streetName, String city);
    Boolean addressExists(AddressDTO addressDTO);
    Page<UserDTO> getAllUsersByAddress(UUID id, Pageable pageable);
}

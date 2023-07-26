package com.ilario.sparkmart.services;

import com.ilario.sparkmart.dto.AddressDTO;
import com.ilario.sparkmart.dto.UserDTO;
import com.ilario.sparkmart.exceptions.addresses.AddressNotFoundException;
import com.ilario.sparkmart.exceptions.addresses.AddressesNotFoundException;
import com.ilario.sparkmart.models.Address;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface IAddressService {
    AddressDTO getById(UUID id) throws AddressNotFoundException;
    void saveToDB(AddressDTO addressDTO);
    Page<AddressDTO> getAll(int page, int pageSize, String sortBy, String sortDir, String keyword) throws AddressesNotFoundException;
    Address getLastSavedAddress() throws AddressNotFoundException;
    Address getAddressByStreetNameAndCity(String streetName, String city) throws AddressNotFoundException;
    Boolean addressExists(AddressDTO addressDTO);
    Page<UserDTO> getAllUsersByAddress(UUID id, int page, int pageSize, String sortDir, String sortBy) throws AddressNotFoundException;
}

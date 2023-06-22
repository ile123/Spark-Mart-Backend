package com.ilario.sparkmart.services;

import com.ilario.sparkmart.dto.AddressDTO;
import com.ilario.sparkmart.dto.UserDTO;
import com.ilario.sparkmart.models.Address;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

public interface IAddressService extends IBaseService<AddressDTO, UUID> {
    Address getLastSavedAddress();
    Address getAddressByStreetNameAndCity(String streetName, String city);
    Boolean addressExists(AddressDTO addressDTO);
    Page<UserDTO> getAllUsersByAddress(UUID id, Pageable pageable);
}

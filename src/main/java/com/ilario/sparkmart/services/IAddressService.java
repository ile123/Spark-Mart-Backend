package com.ilario.sparkmart.services;

import com.ilario.sparkmart.dto.AddressDTO;
import com.ilario.sparkmart.models.Address;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IAddressService {

    AddressDTO getAddressById(UUID id);
    List<AddressDTO> getAllAddresses();
    void saveAddress(AddressDTO addressDTO);
    Address getLastSavedAddress();
    Address getAddressByStreetNameAndCity(String streetName, String city);
    Optional<Address> findAddressById(UUID id);
}

package com.ilario.sparkmart.services;

import com.ilario.sparkmart.dto.AddressDTO;
import com.ilario.sparkmart.models.Address;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IAddressService {

    AddressDTO getAddressById(UUID id);
    List<AddressDTO> getAllAddresses();
    void saveAddress(AddressDTO addressDTO);
    Address getLastSavedAddress();
    Address getAddressByStreetNameAndCity(String streetName, String city);
    Boolean addressExists(AddressDTO addressDTO);
}

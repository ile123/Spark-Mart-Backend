package com.ilario.sparkmart.mappers;

import com.ilario.sparkmart.dto.AddressDTO;
import com.ilario.sparkmart.models.Address;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {


    public AddressDTO toAddressDTO(Address address) {
        return new AddressDTO(address.getId(), address.getStreetAddress(),
                address.getCity(), address.getPostalCode(), address.getProvince(), address.getCountry());
    }

    public Address toAddress(AddressDTO addressDTO) {
        var address = new Address();
        if(addressDTO.id() != null) address.setId(addressDTO.id());
        if(addressDTO.streetAddress() != null) address.setStreetAddress(addressDTO.streetAddress());
        if(addressDTO.city() != null) address.setCity(addressDTO.city());
        if(addressDTO.postalCode() != null) address.setPostalCode(addressDTO.postalCode());
        if(addressDTO.province() != null) address.setProvince(addressDTO.province());
        if(addressDTO.country() != null) address.setCountry(addressDTO.country());
        return address;
    }
}

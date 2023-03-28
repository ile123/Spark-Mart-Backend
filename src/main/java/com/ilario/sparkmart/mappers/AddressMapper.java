package com.ilario.sparkmart.mappers;

import com.ilario.sparkmart.dto.AddressDTO;
import com.ilario.sparkmart.models.Address;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {


    public AddressDTO toAddressDTO(Address address) {
        var addressDTO = new AddressDTO();
        if(address.getId() != null) addressDTO.setId(address.getId());
        if(address.getStreetAddress() != null) addressDTO.setStreetAddress(address.getStreetAddress());
        if(address.getCity() != null) addressDTO.setCity(address.getCity());
        if(address.getPostalCode() != null) addressDTO.setPostalCode(address.getPostalCode());
        if(address.getProvince() != null) addressDTO.setProvince(address.getProvince());
        if(address.getCountry() != null) addressDTO.setCountry(address.getCountry());
        return addressDTO;
    }

    public Address toAddress(AddressDTO addressDTO) {
        var address = new Address();
        if(addressDTO.getId() != null) address.setId(addressDTO.getId());
        if(addressDTO.getStreetAddress() != null) address.setStreetAddress(addressDTO.getStreetAddress());
        if(addressDTO.getCity() != null) address.setCity(addressDTO.getCity());
        if(addressDTO.getPostalCode() != null) address.setPostalCode(addressDTO.getPostalCode());
        if(addressDTO.getProvince() != null) address.setProvince(addressDTO.getProvince());
        if(addressDTO.getCountry() != null) address.setCountry(addressDTO.getCountry());
        return address;
    }
}

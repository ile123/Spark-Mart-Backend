package com.ilario.sparkmart.services.implementations;

import com.ilario.sparkmart.dto.AddressDTO;
import com.ilario.sparkmart.exceptions.addresses.AddressNotFoundException;
import com.ilario.sparkmart.mappers.AddressMapper;
import com.ilario.sparkmart.repositories.IAddressRepository;
import com.ilario.sparkmart.services.IAddressService;
import org.springframework.stereotype.Service;
import com.ilario.sparkmart.models.Address;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class AddressServiceImpl implements IAddressService {
    private final IAddressRepository addressRepository;
    private AddressMapper addressMapper = new AddressMapper();

    public AddressServiceImpl(IAddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public AddressDTO getAddressById(UUID id) {
        var address = addressRepository.findById(id);
        try {
            if(address.isEmpty()) {
                throw new AddressNotFoundException("Address Not Found!");
            }
            return addressMapper.toAddressDTO(address.get());
        } catch (AddressNotFoundException exception) {
            System.out.println(exception.getMessage());
        }
        return null;
    }

    @Override
    public List<AddressDTO> getAllAddresses() {
        var addresses = addressRepository.findAll();
        return addresses.stream().map(addressMapper::toAddressDTO).collect(Collectors.toList());
    }

    @Override
    public void saveAddress(AddressDTO addressDTO) {
        var address = addressMapper.toAddress(addressDTO);
        var existingAddress = addressRepository.findAddressByStreetAddressAndCity(address.getStreetAddress(), address.getCity());
        if(existingAddress.isEmpty()) {
            addressRepository.save(address);
        }
    }

    @Override
    public Address getLastSavedAddress() {
        var addresses = addressRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Address::getCreatedAt))
                .toList();
        return addresses.get(addresses.size() - 1);
    }
//prijasnja verzija je imala if gdje se gledala jeli postoji, ako nepostoji onda se stvara prazna adresa, sprema se i vraca se
    @Override
    public Address getAddressByStreetNameAndCity(String streetName, String city) {
        try {
            var address = addressRepository.findAddressByStreetAddressAndCity(streetName, city);
            if(address.isEmpty()) {
                throw new AddressNotFoundException("ERROR: Address not found!");
            } else {
                return address.get();
            }
        } catch (AddressNotFoundException exception) {
            System.out.println(exception.getMessage());
        }
        return null;
    }

    @Override
    public Boolean addressExists(AddressDTO addressDTO) {
        var existingAddress = addressRepository.findAddressByStreetAddressAndCity(addressDTO.streetAddress(), addressDTO.city());
        return existingAddress.isPresent();
    }

}

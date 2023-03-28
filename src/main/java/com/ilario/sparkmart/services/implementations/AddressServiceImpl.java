package com.ilario.sparkmart.services.implementations;

import com.ilario.sparkmart.dto.AddressDTO;
import com.ilario.sparkmart.exceptions.addresses.AddressNotFoundException;
import com.ilario.sparkmart.mappers.AddressMapper;
import com.ilario.sparkmart.repositories.IAddressRepository;
import com.ilario.sparkmart.services.IAddressService;
import org.springframework.stereotype.Service;
import com.ilario.sparkmart.models.Address;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
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
            if(!address.isPresent()) {
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
        var addressesDTO = addresses.stream().map(addressMapper::toAddressDTO).collect(Collectors.toList());
        return addressesDTO;
    }

    @Override
    public void saveAddress(AddressDTO addressDTO) {
        var address = addressMapper.toAddress(addressDTO);
        addressRepository.save(address);
    }

    @Override
    public Address getLastSavedAddress() {
        var addresses = addressRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Address::getCreatedAt))
                .collect(Collectors.toList());
        return addresses.get(addresses.size() - 1);
    }

    @Override
    public Address getAddressByStreetNameAndCity(String streetName, String city) {
        return addressRepository.findAddressByStreetAddressAndCity(streetName, city);
    }

    @Override
    public Optional<Address> findAddressById(UUID id) {
        return addressRepository.findById(id);
    }

}

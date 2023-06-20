package com.ilario.sparkmart.services.implementations;

import com.ilario.sparkmart.dto.AddressDTO;
import com.ilario.sparkmart.exceptions.addresses.AddressNotFoundException;
import com.ilario.sparkmart.mappers.AddressMapper;
import com.ilario.sparkmart.repositories.IAddressRepository;
import com.ilario.sparkmart.services.IAddressService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import com.ilario.sparkmart.models.Address;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class AddressServiceImplIAddressService implements IAddressService {
    private final IAddressRepository addressRepository;
    private final AddressMapper addressMapper = new AddressMapper();

    public AddressServiceImplIAddressService(IAddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public List<AddressDTO> getAllAddresses() {
        var addresses = addressRepository.findAll();
        return addresses.stream().map(addressMapper::toAddressDTO).collect(Collectors.toList());
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

    @Override
    public AddressDTO getById(UUID uuid) {
        var address = addressRepository.findById(uuid);
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
    public void saveToDB(AddressDTO entity) {
        var address = addressMapper.toAddress(entity);
        var existingAddress = addressRepository.findAddressByStreetAddressAndCity(address.getStreetAddress(), address.getCity());
        if(existingAddress.isEmpty()) {
            addressRepository.save(address);
        }
    }

    @Override
    public Page<AddressDTO> getAll(int page, int pageSize, String sortBy, String sortDir, String keyword) {
        Pageable pageable = PageRequest.of(
                page,
                pageSize,
                sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());
        var pageResult = addressRepository.findAll(pageable);
        var addressDTOs = pageResult
                .getContent()
                .stream()
                .filter(x -> !x.getStreetAddress().isEmpty())
                .map(addressMapper::toAddressDTO)
                .toList();
        return new PageImpl<>(addressDTOs, pageable, pageResult.getTotalElements());
    }

    @Override
    public void update(UUID uuid, AddressDTO entity) { }

    @Override
    public void delete(UUID uuid) { }
}

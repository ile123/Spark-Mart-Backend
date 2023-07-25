package com.ilario.sparkmart.services.implementations;

import com.ilario.sparkmart.dto.AddressDTO;
import com.ilario.sparkmart.dto.UserDTO;
import com.ilario.sparkmart.exceptions.addresses.AddressNotFoundException;
import com.ilario.sparkmart.exceptions.addresses.AddressesNotFoundException;
import com.ilario.sparkmart.mappers.AddressMapper;
import com.ilario.sparkmart.mappers.UserMapper;
import com.ilario.sparkmart.models.User;
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
public class AddressServiceImpl implements IAddressService {
    private final IAddressRepository addressRepository;
    private final AddressMapper addressMapper = new AddressMapper();

    private final UserMapper userMapper = new UserMapper();

    public AddressServiceImpl(IAddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public Address getLastSavedAddress() {
        var lastAddress = addressRepository
                .findAll()
                .stream()
                .max(Comparator.comparing(Address::getCreatedAt));
        return lastAddress.orElseThrow();
    }

    @Override
    public Address getAddressByStreetNameAndCity(String streetName, String city) throws AddressNotFoundException {
        var address = addressRepository.findAddressByStreetAddressAndCity(streetName, city);
        if(address.isEmpty()) {
            throw new AddressNotFoundException("ERROR: Address by given street and name not found.");
        }
        return address.get();
    }

    @Override
    public Boolean addressExists(AddressDTO addressDTO) {
        return addressRepository
                .findAddressByStreetAddressAndCity(addressDTO.streetAddress(), addressDTO.city())
                .isPresent();
    }

    @Override
    public Page<UserDTO> getAllUsersByAddress(UUID id, Pageable pageable) throws AddressNotFoundException {
        var address = addressRepository.findById(id)
                .orElseThrow(() -> new AddressNotFoundException("ERROR: Address by given ID not found."));
        var users = address.getUsers()
                .stream()
                .filter(User::isEnabled)
                .map(userMapper::toUserDTO)
                .toList();
        return new PageImpl<>(users, pageable, users.size());
    }

    @Override
    public AddressDTO getById(UUID uuid) throws AddressNotFoundException {
        var address = addressRepository.findById(uuid);
        if(address.isEmpty()) {
            throw new AddressNotFoundException("ERROR: Address by given id not found.");
        }
        return addressMapper.toAddressDTO(address.get());
    }

    @Override
    public void saveToDB(AddressDTO addressDTO) {
        var address = addressMapper.toAddress(addressDTO);
        var existingAddress = addressRepository.findAddressByStreetAddressAndCity(address.getStreetAddress(), address.getCity());
        if(existingAddress.isEmpty()) {
            addressRepository.save(address);
        }
    }

    @Override
    public Page<AddressDTO> getAll(int page, int pageSize, String sortBy, String sortDir, String keyword) throws AddressesNotFoundException {
        var pageable = PageRequest.of(
                page,
                pageSize,
                sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());
        var pageResult = keyword.isEmpty() ?
                addressRepository.findAll(pageable) :
                addressRepository.findAllByKeyword(keyword.toLowerCase(), pageable);
        if(pageResult.isEmpty()) {
            throw new AddressesNotFoundException("ERROR: Addresses not found.");
        }
        var addressDTOs = pageResult
                .getContent()
                .stream()
                .filter(x -> !x.getStreetAddress().isEmpty())
                .map(addressMapper::toAddressDTO)
                .collect(Collectors.toList());
        return new PageImpl<>(addressDTOs, pageable, pageResult.getTotalElements());
    }
}

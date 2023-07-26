package com.ilario.sparkmart.services.implementations;

import com.ilario.sparkmart.dto.AddressDTO;
import com.ilario.sparkmart.dto.UserDTO;
import com.ilario.sparkmart.exceptions.addresses.AddressCouldNotBeMappedException;
import com.ilario.sparkmart.exceptions.addresses.AddressNotFoundException;
import com.ilario.sparkmart.exceptions.users.UserEmailAlreadyInUseException;
import com.ilario.sparkmart.exceptions.users.UserNotFoundException;
import com.ilario.sparkmart.mappers.UserMapper;
import com.ilario.sparkmart.models.User;
import com.ilario.sparkmart.security.misc.enums.Role;
import org.springframework.data.domain.*;
import com.ilario.sparkmart.repositories.IUserRepository;
import com.ilario.sparkmart.services.IUserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements IUserService {

    private final IUserRepository userRepository;
    private final AddressServiceImpl addressService;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper = new UserMapper();

    public UserServiceImpl(IUserRepository userRepository, AddressServiceImpl addressService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.addressService = addressService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDTO getUserByEmail(String email) throws UserNotFoundException {
        var user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("ERROR: User by given email not found."));
        return userMapper.toUserDTO(user);
    }

    @Override
    public void updateAddress(UUID userId, AddressDTO addressDTO) throws UserNotFoundException, AddressCouldNotBeMappedException, AddressNotFoundException {
        var user = userRepository
                .findById(userId)
                .orElseThrow(() -> new UserNotFoundException("ERROR: User by given ID not found."));
        if(addressDTO == null) {
            throw new AddressCouldNotBeMappedException("ERROR: Address could not be mapped from AddressDTO");
        }
        var existingAddress = addressService
                .getAddressByStreetNameAndCity(addressDTO.streetAddress(), addressDTO.city());
        if(existingAddress != null) {
            user.setAddress(existingAddress);
            existingAddress.getUsers().add(user);
        } else {
            addressService.saveToDB(addressDTO);
            var address = addressService.getLastSavedAddress();
            user.setAddress(address);
            address.getUsers().add(user);
        }
        userRepository.save(user);
    }

    @Override
    public Boolean checkIfEmailIsAlreadyUsed(String email) {
        return userRepository
                .findByEmail(email)
                .isPresent();
    }

    @Override
    public void changeUserPassword(UUID userId, String newPassword) throws UserNotFoundException {
            var user = userRepository
                    .findById(userId)
                    .orElseThrow(() -> new UserNotFoundException("ERROR: User not found by given ID."));
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
    }

    @Override
    public Boolean doesAdministratorExist() {
        return userRepository.doesAdministratorExist();
    }

    @Override
    public UserDTO getById(UUID uuid) throws UserNotFoundException {
        var user = userRepository
                .findById(uuid)
                .orElseThrow(() -> new UserNotFoundException("ERROR: User not found by given ID."));
        return userMapper.toUserDTO(user);
    }

    @Override
    public void saveToDB(UserDTO entity) throws UserEmailAlreadyInUseException {
        var user = userRepository
                .findByEmail(entity.email());
        if(user.isEmpty()) {
            userRepository.save(userMapper.toUser(entity));
        } else {
            throw new UserEmailAlreadyInUseException("ERROR: User by given email already in use.");
        }
    }

    @Override
    public Page<UserDTO> getAll(int page, int pageSize, String sortBy, String sortDir, String userType, String keyword) {
        var pageable = PageRequest.of(
                page,
                pageSize,
                sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());
        var pageResult = keyword.isEmpty() ?
                userRepository.findAllByRole(Role.valueOf(userType.toUpperCase()), pageable) :
                userRepository.findAllByKeyword(Role.valueOf(userType.toUpperCase()), keyword.toLowerCase(), pageable);
        var userDTOs = pageResult
                .getContent()
                .stream()
                .filter(User::isEnabled)
                .map(userMapper::toUserDTO)
                .collect(Collectors.toList());
        return new PageImpl<>(userDTOs, pageable, pageResult.getTotalElements());
    }

    @Override
    public void update(UUID uuid, UserDTO entity) throws UserNotFoundException {
        var user = userRepository
                .findById(uuid)
                .orElseThrow(() -> new UserNotFoundException("ERROR: User by given ID not found."));
        user.setFirstName(entity.firstName());
        user.setLastName(entity.lastName());
        user.setPhoneNumber(entity.phoneNumber());
        userRepository.save(user);
    }

    @Override
    public void delete(UUID uuid) throws UserNotFoundException {
        var user = userRepository
                .findById(uuid)
                .orElseThrow(() -> new UserNotFoundException("ERROR: User by given ID not found."));
        if(!user.getOrders().isEmpty() || !user.getWishlist().getProducts().isEmpty()) {
            user.setDisabled(true);
            userRepository.save(user);
        } else {
            userRepository.delete(user);
        }
    }
}

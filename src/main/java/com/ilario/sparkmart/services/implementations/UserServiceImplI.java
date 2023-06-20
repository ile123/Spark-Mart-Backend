package com.ilario.sparkmart.services.implementations;

import com.ilario.sparkmart.dto.AddressDTO;
import com.ilario.sparkmart.dto.UserDTO;
import com.ilario.sparkmart.exceptions.addresses.AddressCouldNotBeMappedException;
import com.ilario.sparkmart.exceptions.users.UserNotFoundException;
import com.ilario.sparkmart.mappers.UserMapper;
import com.ilario.sparkmart.models.User;
import com.ilario.sparkmart.security.misc.Role;
import org.springframework.data.domain.*;
import com.ilario.sparkmart.repositories.IUserRepository;
import com.ilario.sparkmart.services.IUserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceImplI implements IUserService {

    private final IUserRepository userRepository;
    private final AddressServiceImplIAddressService addressService;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper = new UserMapper();

    public UserServiceImplI(IUserRepository userRepository, AddressServiceImplIAddressService addressService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.addressService = addressService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        try {
            var user = userRepository.findByEmail(email);
            if(user.isEmpty()) {
                throw new UserNotFoundException("ERROR: User not found by given ID!");
            }
            return userMapper.toUserDTO(user.get());
        } catch (UserNotFoundException exception) {
            System.out.println(exception.getMessage());
        }
        return null;
    }

    @Override
    public void updateAddress(UUID userId, AddressDTO addressDTO) {
        try {
            var userOptional = userRepository.findById(userId);
            if (userOptional.isEmpty()) {
                throw new UserNotFoundException("ERROR: User not found by given ID!");
            }
            if(addressDTO == null) {
                throw new AddressCouldNotBeMappedException("ERROR: Address could not be mapped from AddressDTO");
            }
            var user = userOptional.get();
            var existingAddress = addressService.getAddressByStreetNameAndCity(addressDTO.streetAddress(),
                    addressDTO.city());
            if(existingAddress != null) {
                user.setAddress(existingAddress);
                userRepository.save(user);
            } else {
                addressService.saveToDB(addressDTO);
                var address = addressService.getLastSavedAddress();
                user.setAddress(address);
                userRepository.save(user);
            }
        } catch (AddressCouldNotBeMappedException | UserNotFoundException exception) {
                System.out.println(exception.getMessage());
        }
    }

    @Override
    public Boolean checkIfEmailIsAlreadyUsed(String email) {
        var existingUser = userRepository.findByEmail(email);
        return existingUser.isPresent();
    }

    @Override
    public void changeUserPassword(UUID userId, String newPassword) {
        try {
            var user = userRepository.findById(userId);
            if(user.isEmpty()) {
                throw new UserNotFoundException("ERROR: User not found!");
            }
            user.get().setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user.get());
        } catch (UserNotFoundException exception) {
            System.out.println(exception.getMessage());
        }
    }

    @Override
    public Boolean doesAdministratorExist() {
        return userRepository.doesAdministratorExist();
    }

    @Override
    public UserDTO getById(UUID uuid) {
        try {
            var user = userRepository.findById(uuid);
            if(user.isEmpty()) {
                throw new UserNotFoundException("ERROR: User not found by given ID!");
            }
            return userMapper.toUserDTO(user.get());
        } catch (UserNotFoundException exception) {
            System.out.println(exception.getMessage());
        }
        return null;
    }

    @Override
    public void saveToDB(UserDTO entity) {
        userRepository.save(userMapper.toUser(entity));
    }

    @Override
    public Page<UserDTO> getAll(int page, int pageSize, String sortBy, String sortDir, String keyword) {
        Pageable pageable = PageRequest.of(
                page,
                pageSize,
                sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());
        Page<User> pageResult;
        if(keyword.isEmpty()) {
            pageResult = userRepository.findAll(pageable);
        }
        else {
            pageResult = userRepository.findAllByKeyword(null, keyword.toLowerCase(), pageable);
        }
        var userDTOs = pageResult
                .getContent()
                .stream()
                .filter(User::isEnabled)
                .map(userMapper::toUserDTO)
                .toList();
        return new PageImpl<>(userDTOs, pageable, pageResult.getTotalElements());
    }

    @Override
    public Page<UserDTO> getAll(int page, int pageSize, String sortBy, String sortDir, String userType, String keyword) {
        Pageable pageable = PageRequest.of(
                page,
                pageSize,
                sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());
        System.out.println(userType);
        Page<User> pageResult;
        if(keyword.isEmpty()) {
            pageResult = userRepository.findAllByRole(Role.valueOf(userType.toUpperCase()), pageable);
        }
        else {
            pageResult = userRepository.findAllByKeyword(Role.valueOf(userType.toUpperCase()), keyword.toLowerCase(), pageable);
        }
        var userDTOs = pageResult
                .getContent()
                .stream()
                .filter(User::isEnabled)
                .map(userMapper::toUserDTO)
                .toList();
        return new PageImpl<>(userDTOs, pageable, pageResult.getTotalElements());
    }

    @Override
    public void update(UUID uuid, UserDTO entity) {
        try {
            var userOptional = userRepository.findById(uuid);
            if(userOptional.isEmpty()) {
                throw new UserNotFoundException("ERROR: User not found by given ID!");
            }
            var user = userOptional.get();
            if((!Objects.equals(user.getFirstName(), entity.firstName())) && (!entity.firstName().isEmpty() && !entity.firstName().isBlank())) {
                user.setFirstName(entity.firstName());
            }
            if((!Objects.equals(user.getLastName(), entity.lastName())) && (!entity.lastName().isEmpty() && !entity.lastName().isBlank())) {
                user.setLastName(entity.lastName());
            }
            if((!Objects.equals(user.getPhoneNumber(), entity.phoneNumber())) && (!entity.phoneNumber().isEmpty() && !entity.phoneNumber().isBlank())) {
                user.setPhoneNumber(entity.phoneNumber());
            }
            userRepository.save(user);
        } catch (UserNotFoundException exception) {
            System.out.println(exception.getMessage());
        }
    }

    @Override
    public void delete(UUID uuid) {
        try {
            if(userRepository.findById(uuid).isEmpty()) {
                throw new UserNotFoundException("ERROR: User not found by given ID!");
            }
            userRepository.deleteById(uuid);
        } catch (UserNotFoundException exception) {
            System.out.println(exception.getMessage());
        }
    }
}

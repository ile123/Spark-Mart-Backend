package com.ilario.sparkmart.services.implementations;

import com.ilario.sparkmart.dto.AddressDTO;
import com.ilario.sparkmart.dto.UserDTO;
import com.ilario.sparkmart.exceptions.addresses.AddressCouldNotBeMappedException;
import com.ilario.sparkmart.exceptions.users.UserNotFoundException;
import com.ilario.sparkmart.mappers.UserMapper;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import com.ilario.sparkmart.repositories.IUserRepository;
import com.ilario.sparkmart.services.IUserService;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
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
    public UserDTO getUserById(UUID id) {
        try {
            var user = userRepository.findById(id);
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
    public Page<UserDTO> getAllUsers(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        var pageResult = userRepository.findAll(pageable);
        var userDTOs = pageResult
                .getContent()
                .stream()
                .map(userMapper::toUserDTO)
                .toList();
        return new PageImpl<>(userDTOs, pageable, pageResult.getTotalElements());
        //return allUsers.stream().map(userMapper::toUserDTO).collect(Collectors.toList());
    }

    @Override
    public void updateUser(UUID userId, UserDTO userDTO) {
        try {
            var userOptional = userRepository.findById(userId);
            if(userOptional.isEmpty()) {
                throw new UserNotFoundException("ERROR: User not found by given ID!");
            }
            var user = userOptional.get();
            if((!Objects.equals(user.getFirstName(), userDTO.firstName())) && (!userDTO.firstName().isEmpty() && !userDTO.firstName().isBlank())) {
                user.setFirstName(userDTO.firstName());
            }
            if((!Objects.equals(user.getLastName(), userDTO.lastName())) && (!userDTO.lastName().isEmpty() && !userDTO.lastName().isBlank())) {
                user.setLastName(userDTO.lastName());
            }
            if((!Objects.equals(user.getPhoneNumber(), userDTO.phoneNumber())) && (!userDTO.phoneNumber().isEmpty() && !userDTO.phoneNumber().isBlank())) {
                user.setPhoneNumber(userDTO.phoneNumber());
            }
            userRepository.save(user);
        } catch (UserNotFoundException exception) {
            System.out.println(exception.getMessage());
        }
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
                addressService.saveAddress(addressDTO);
                var address = addressService.getLastSavedAddress();
                user.setAddress(address);
                userRepository.save(user);
            }
        } catch (AddressCouldNotBeMappedException | UserNotFoundException exception) {
                System.out.println(exception.getMessage());
        }
    }

    @Override
    public void deleteUser(UUID userID) {
        try {
            if(userRepository.findById(userID).isEmpty()) {
                throw new UserNotFoundException("ERROR: User not found by given ID!");
            }
            userRepository.deleteById(userID);
        } catch (UserNotFoundException exception) {
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
}

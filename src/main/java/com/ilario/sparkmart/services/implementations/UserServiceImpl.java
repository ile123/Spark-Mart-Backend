package com.ilario.sparkmart.services.implementations;

import com.ilario.sparkmart.dto.AddressDTO;
import com.ilario.sparkmart.dto.UserDTO;
import com.ilario.sparkmart.exceptions.addresses.AddressCouldNotBeMappedException;
import com.ilario.sparkmart.exceptions.addresses.AddressNotFoundException;
import com.ilario.sparkmart.exceptions.users.UserNotFoundException;
import com.ilario.sparkmart.mappers.AddressMapper;
import com.ilario.sparkmart.mappers.UserMapper;
import com.ilario.sparkmart.models.Wishlist;
import com.ilario.sparkmart.repositories.IUserRepository;
import com.ilario.sparkmart.services.IUserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements IUserService {

    private final IUserRepository userRepository;
    private final AddressServiceImpl addressService;
    private final RoleServiceImpl roleService;
    private final WishlistServiceImpl wishlistService;
    private final UserMapper userMapper = new UserMapper();
    private final AddressMapper addressMapper = new AddressMapper();

    public UserServiceImpl(IUserRepository userRepository, AddressServiceImpl addressService, RoleServiceImpl roleService, WishlistServiceImpl wishlistService) {
        this.userRepository = userRepository;
        this.addressService = addressService;
        this.roleService = roleService;
        this.wishlistService = wishlistService;
    }

    @Override
    public UserDTO getUserById(UUID id) {
        var user = userRepository.findById(id).get();
        return userMapper.toUserDTO(user);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        var allUsers = userRepository.findAll();
        return allUsers.stream().map(userMapper::toUserDTO).collect(Collectors.toList());
    }
    @Override
    public void saveUser(UserDTO userDTO) {
        var user = userMapper.toUser(userDTO);
        var role = roleService.getRole(userDTO.getRoleId()).get();
        var wishlist = new Wishlist();
        var address = addressService.getAddressByStreetNameAndCity("", "");
        user.setRole(role);
        wishlist.setUser(user);
        user.setWishlist(wishlist);
        user.setAddress(address);
        userRepository.save(user);
    }

    @Override
    public void updateUser(UUID userId, UserDTO userDTO) {
        var user = userRepository.findById(userId).get();
        if((user.getUsername() != userDTO.getUsername()) && (!userDTO.getUsername().isEmpty() && !userDTO.getUsername().isBlank())) {
            user.setUsername(userDTO.getUsername());
        }
        if((user.getEmail() != userDTO.getEmail()) && (!userDTO.getEmail().isEmpty() && !userDTO.getEmail().isBlank())) {
            user.setEmail(userDTO.getEmail());
        }
        if((user.getFirstName() != userDTO.getFirstName()) && (!userDTO.getFirstName().isEmpty() && !userDTO.getFirstName().isBlank())) {
            user.setFirstName(userDTO.getFirstName());
        }
        if((user.getLastName() != userDTO.getLastName()) && (!userDTO.getLastName().isEmpty() && !userDTO.getLastName().isBlank())) {
            user.setLastName(userDTO.getLastName());
        }
        if((user.getPhoneNumber() != userDTO.getPhoneNumber()) && (!userDTO.getPhoneNumber().isEmpty() && !userDTO.getPhoneNumber().isBlank())) {
            user.setPhoneNumber(userDTO.getPhoneNumber());
        }
        userRepository.save(user);
    }

    @Override
    public void updateAddress(UUID userId, AddressDTO addressDTO) {
        var user = userRepository.findById(userId).get();
        try {
            if(addressDTO == null) {
                throw new AddressCouldNotBeMappedException("Address could not be mapped from AddressDTO");
            }
            var existingAddress = addressService.getAddressByStreetNameAndCity(addressDTO.getStreetAddress(),
                    addressDTO.getCity());
            if(existingAddress != null) {
                user.setAddress(existingAddress);
                userRepository.save(user);
            } else {
                addressService.saveAddress(addressDTO);
                var address = addressService.getLastSavedAddress();
                user.setAddress(address);
                userRepository.save(user);
            }
        } catch (AddressCouldNotBeMappedException exception) {
                System.out.println(exception.getMessage());
        }
    }
//07e153f5-637c-403f-a2eb-e6dc6154bb62
    @Override
    public void deleteUser(UUID userID) {
        try {
            if(userRepository.findById(userID).isEmpty()) {
                throw new UserNotFoundException("User Not Found!");
            }
            userRepository.deleteById(userID);
        } catch (UserNotFoundException exception) {
            System.out.println(exception.getMessage());
        }
    }
}

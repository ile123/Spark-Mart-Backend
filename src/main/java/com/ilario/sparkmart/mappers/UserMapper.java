package com.ilario.sparkmart.mappers;

import com.ilario.sparkmart.dto.UserDTO;
import com.ilario.sparkmart.models.User;
import com.ilario.sparkmart.services.implementations.AddressServiceImpl;
import com.ilario.sparkmart.services.implementations.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    @Autowired
    private AddressServiceImpl addressService;
    @Autowired
    private UserServiceImpl userService;
    private final AddressMapper addressMapper = new AddressMapper();
    public UserDTO toUserDTO(User user) {
        var addressDTO = addressMapper.toAddressDTO(user.getAddress());
        return new UserDTO(user.getId(), user.getWishlist().getId(), user.getEmail(),
                user.getFirstName(), user.getLastName(),
                user.getPhoneNumber(), user.getRole().toString(), addressDTO);
    }

    public User toUser(UserDTO userDTO) {
        var user = new User();
        if(userDTO.id() != null) user.setId(userDTO.id());
        if(userDTO.email() != null) user.setEmail(userDTO.email());
        if(userDTO.firstName() != null) user.setFirstName(userDTO.firstName());
        if(userDTO.lastName() != null) user.setLastName(userDTO.lastName());
        if(userDTO.phoneNumber() != null) user.setPhoneNumber(userDTO.phoneNumber());
        return user;
    }
}

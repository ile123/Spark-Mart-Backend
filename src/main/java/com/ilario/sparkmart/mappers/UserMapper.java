package com.ilario.sparkmart.mappers;

import com.ilario.sparkmart.dto.UserDTO;
import com.ilario.sparkmart.models.User;
import com.ilario.sparkmart.services.implementations.AddressServiceImpl;
import com.ilario.sparkmart.services.implementations.RoleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    @Autowired
    private RoleServiceImpl roleService;
    @Autowired
    private AddressServiceImpl addressService;
    public UserDTO toUserDTO(User user) {
        var userDTO = new UserDTO();
        if(user.getId() != null) userDTO.setId(user.getId());
        if(user.getUsername() != null) userDTO.setUsername(user.getUsername());
        if(user.getEmail() != null) userDTO.setEmail(user.getEmail());
        if(user.getFirstName() != null) userDTO.setFirstName(user.getFirstName());
        if(user.getLastName() != null) userDTO.setLastName(user.getLastName());
        if(user.getPhoneNumber() != null) userDTO.setPhoneNumber(user.getPhoneNumber());
        if(user.getRole() != null) userDTO.setRoleId(user.getAddress().getId());
        if(user.getWishlist() != null) userDTO.setWishListId(user.getWishlist().getId());
        if(user.getAddress() != null) userDTO.setAddressId(user.getAddress().getId());
        return userDTO;
    }

    public User toUser(UserDTO userDTO) {
        var user = new User();
        if(userDTO.getId() != null) user.setId(userDTO.getId());
        if(userDTO.getUsername() != null) user.setUsername(userDTO.getUsername());
        if(userDTO.getEmail() != null) user.setEmail(userDTO.getEmail());
        if(userDTO.getFirstName() != null) user.setFirstName(userDTO.getFirstName());
        if(userDTO.getLastName() != null) user.setLastName(userDTO.getLastName());
        if(userDTO.getPhoneNumber() != null) user.setPhoneNumber(userDTO.getPhoneNumber());
        return user;
    }
}

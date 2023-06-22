package com.ilario.sparkmart.mappers;

import com.ilario.sparkmart.dto.UserDTO;
import com.ilario.sparkmart.models.User;
import com.ilario.sparkmart.security.misc.enums.Gender;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserDTO toUserDTO(User user) {
        return new UserDTO(user.getId(), user.getWishlist().getId(), user.getEmail(),
                user.getFirstName(), user.getLastName(),
                user.getPhoneNumber(), user.getGender().toString(), user.getRole().toString(), user.getAddress().getId());
    }

    public User toUser(UserDTO userDTO) {
        var user = new User();
        if(userDTO.id() != null) user.setId(userDTO.id());
        if(userDTO.email() != null) user.setEmail(userDTO.email());
        if(userDTO.firstName() != null) user.setFirstName(userDTO.firstName());
        if(userDTO.lastName() != null) user.setLastName(userDTO.lastName());
        if(userDTO.phoneNumber() != null) user.setPhoneNumber(userDTO.phoneNumber());
        if(userDTO.gender() != null) user.setGender(Gender.valueOf(userDTO.gender()));
        return user;
    }
}

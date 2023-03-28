package com.ilario.sparkmart.users;

import com.ilario.sparkmart.dto.UserDTO;
import com.ilario.sparkmart.mappers.UserMapper;
import com.ilario.sparkmart.models.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
@DataJpaTest
public class UserDTOTests {

    UserMapper mapper = new UserMapper();
    @Test
    void CreateUserDTOTest() {
        var userDTO = new UserDTO();
        assertThat(userDTO).isInstanceOf(userDTO.getClass());
    }

    @Test
    void CreateUserDTOTestAndGiveItOneValueTest() {
        var userDTO = new UserDTO();
        userDTO.setId(UUID.randomUUID());
        assertThat(userDTO.getId()).isNotEqualTo(null);
        assertThat(userDTO.getUsername()).isEqualTo(null);
    }

    @Test
    void ConvertFromUserToUserDTOTest() {
        var user = new User();
        var userDTO = mapper.toUserDTO(user);
        assertThat(userDTO).isInstanceOf(userDTO.getClass());
    }
}

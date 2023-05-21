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
    void CreateUserDTOTestAndGiveItValues() {
        var userDTO = new UserDTO(UUID.randomUUID(), "Temp", "Temp",
                "Temp", "Temp", "Temp",
                 UUID.randomUUID(), UUID.randomUUID());
        assertThat(userDTO.id()).isNotEqualTo(null);
    }

}

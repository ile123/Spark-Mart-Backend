package com.ilario.sparkmart.users;

import com.ilario.sparkmart.dto.UserDTO;
import com.ilario.sparkmart.mappers.UserMapper;
import com.ilario.sparkmart.models.Address;
import com.ilario.sparkmart.models.User;
import com.ilario.sparkmart.models.Wishlist;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
@DataJpaTest
public class UserTests {

    UserMapper mapper = new UserMapper();

    @Test
    void CreateUserTest() {
        var user = new User();
        assertThat(user).isInstanceOf(User.class);
    }

    @Test
    void CreateUserAndLinkRoleAndAddressAndWishlistTest() {
        var user = new User();
        user.setAddress(new Address());
        user.setWishlist(new Wishlist());
        assertThat(user.getAddress()).isInstanceOf(Address.class);
        assertThat(user.getWishlist()).isInstanceOf(Wishlist.class);
    }

    @Test
    void ConvertUserDTOToUserTest() {
        var userDTO = new UserDTO(UUID.randomUUID(), "Temp",
                "Temp", "Temp", "Temp",
                "Temp",  UUID.randomUUID(), UUID.randomUUID());
        var user = mapper.toUser(userDTO);
        assertThat(user).isInstanceOf(User.class);
    }

}

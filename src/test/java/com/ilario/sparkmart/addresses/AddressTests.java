package com.ilario.sparkmart.addresses;

import com.ilario.sparkmart.mappers.AddressMapper;
import com.ilario.sparkmart.models.Address;
import com.ilario.sparkmart.models.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
@DataJpaTest
public class AddressTests {

    @Test
    void CreateAddressTest() {
        var address = new Address();
        assertThat(address).isInstanceOf(Address.class);
    }

    @Test
    void CreateAddressAndLinkUserTest() {
        var user = new User();
        var address = new Address();
        address.getUsers().add(user);
        assertThat(address.getUsers()).isNotEqualTo(address.getUsers().isEmpty());
    }

    @Test
    void ConvertAddressToAddressDTO() {
        var address = new Address();
        address.setId(UUID.randomUUID());
        address.setStreetAddress("Vukovarska 2");
        address.setCity("Split");
        address.setPostalCode("21000");
        address.setCountry("Croatia");
        var addressDTO = AddressMapper.toAddressDTO(address);
        assertThat(addressDTO.getId()).isEqualTo(address.getId());
    }
}

package com.ilario.sparkmart.addresses;

import com.ilario.sparkmart.dto.AddressDTO;
import com.ilario.sparkmart.mappers.AddressMapper;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
public class AddressDTOTests {
    @Test
    void CreateAddressDTOTest() {
        var addressDto = new AddressDTO();
        assertThat(addressDto).isInstanceOf(addressDto.getClass());
    }

    @Test
    void CreateAddressDTOAndGiveOneValue(){
        var addressDTO1 = new AddressDTO();
        addressDTO1.setId(UUID.randomUUID());
        assertThat(addressDTO1.getId()).isNotEqualTo(null);
        assertThat(addressDTO1.getCity()).isEqualTo(null);
    }

    @Test
    void ConvertDTOToAddressEntity() {
        var addressDTO = new AddressDTO(UUID.randomUUID(), "Vukovarska 2",
                "Split", "21000", "Splitsko-Dalmatinska Zupanija","Croatia");
        var address = AddressMapper.toAddress(addressDTO);
        assertThat(address.getId()).isEqualTo(addressDTO.getId());
    }
}

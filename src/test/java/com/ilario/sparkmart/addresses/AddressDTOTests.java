package com.ilario.sparkmart.addresses;

import com.ilario.sparkmart.dto.AddressDTO;
import com.ilario.sparkmart.mappers.AddressMapper;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
public class AddressDTOTests {

    AddressMapper addressMapper = new AddressMapper();

    @Test
    void CreateAddressDTOTest() {
        var addressDto = new AddressDTO(UUID.randomUUID(), "Temp", "Temp"
        , "Temp", "Temp", "Temp");
        assertThat(addressDto).isInstanceOf(addressDto.getClass());
    }

    @Test
    void CreateAddressDTOAndGiveValues(){
        var addressDTO1 = new AddressDTO(UUID.randomUUID(), "Temp", "Temp", "Temp", "Temp", "Temp");
        assertThat(addressDTO1.id()).isNotEqualTo(null);
        assertThat(addressDTO1.city()).isEqualTo(null);
    }

    @Test
    void ConvertDTOToAddressEntity() {
        var addressDTO = new AddressDTO(UUID.randomUUID(), "Vukovarska 2",
                "Split", "21000", "Splitsko-Dalmatinska Zupanija","Croatia");
        var address = addressMapper.toAddress(addressDTO);
        assertThat(address.getId()).isEqualTo(addressDTO.id());
    }
}

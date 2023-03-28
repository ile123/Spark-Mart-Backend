package com.ilario.sparkmart.addresses;

import com.ilario.sparkmart.models.Address;
import com.ilario.sparkmart.repositories.IAddressRepository;
import com.ilario.sparkmart.services.implementations.AddressServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
@ExtendWith(MockitoExtension.class)
public class AddressServiceTests {
    @Mock
    private IAddressRepository addressRepository;
    private AddressServiceImpl addressService;
    @BeforeEach
    void setUp() {
        addressService = new AddressServiceImpl(addressRepository);
    }

    @Test
    void GetAllAddressesTest() {
        addressRepository.findAll();
        Mockito.verify(addressRepository).findAll();
    }

    @Test
    void GetAddressByIdTest() {
        var address = new Address();
        addressRepository.save(address);
        var temp = addressRepository.findAll().get(0);
        var newAddress = addressRepository.findById(temp.getId());
        assertThat(newAddress).isInstanceOf(Address.class);
    }

    @Test
    void SaveAddressTest() {
        var address = new Address();
        addressRepository.save(address);
        Mockito.verify(addressRepository).save(address);
    }
}

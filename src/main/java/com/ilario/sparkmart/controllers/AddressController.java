package com.ilario.sparkmart.controllers;

import com.ilario.sparkmart.dto.AddressDTO;
import com.ilario.sparkmart.services.implementations.AddressServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/addresses")
public class AddressController {
    private final AddressServiceImpl addressService;

    public AddressController(AddressServiceImpl addressService) {
        this.addressService = addressService;
    }

    @GetMapping("")
    public ResponseEntity<List<AddressDTO>> GetAllAddresses() {
        var addresses = addressService.getAllAddresses();
        if(addresses.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(addresses, HttpStatus.OK);
    }

    @GetMapping("/{addressId}")
    public ResponseEntity<AddressDTO> GetAddress(@PathVariable UUID addressId) {
        var address = addressService.getAddressById(addressId);
        return new ResponseEntity<>(address, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<String> SaveAddress(@RequestBody AddressDTO addressDTO) {
        if(addressDTO == null) {
            return new ResponseEntity<>("Could not save Address!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if(addressService.addressExists(addressDTO)) {
            return new ResponseEntity<>("ERROR: Address already exists!", HttpStatus.BAD_REQUEST);
        }
        addressService.saveAddress(addressDTO);
        return new ResponseEntity<>("Address successfully saved successfully!", HttpStatus.OK);
    }
}
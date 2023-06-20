package com.ilario.sparkmart.controllers;

import com.ilario.sparkmart.dto.AddressDTO;
import com.ilario.sparkmart.services.implementations.AddressServiceImplIAddressService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/addresses")
public class AddressController {
    private final AddressServiceImplIAddressService addressService;

    public AddressController(AddressServiceImplIAddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping("")
    public ResponseEntity<Page<AddressDTO>> GetAllAddresses(
                @RequestParam(defaultValue = "0") int page,
                @RequestParam(defaultValue = "10") int pageSize,
                @RequestParam(defaultValue = "streetAddress") String sortBy,
                @RequestParam(defaultValue = "asc") String sortDir,
                @RequestParam(defaultValue = "") String keyword) {
        var addresses = addressService.getAll(page, pageSize, sortBy, sortDir, keyword);
        if(addresses.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(addresses, HttpStatus.OK);
    }

    @GetMapping("/{addressId}")
    public ResponseEntity<AddressDTO> GetAddress(@PathVariable UUID addressId) {
        var address = addressService.getById(addressId);
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
        addressService.saveToDB(addressDTO);
        return new ResponseEntity<>("Address successfully saved successfully!", HttpStatus.OK);
    }
}

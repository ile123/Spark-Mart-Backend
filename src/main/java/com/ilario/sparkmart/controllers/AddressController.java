package com.ilario.sparkmart.controllers;

import com.ilario.sparkmart.dto.AddressDTO;
import com.ilario.sparkmart.dto.UserDTO;
import com.ilario.sparkmart.exceptions.addresses.AddressNotFoundException;
import com.ilario.sparkmart.services.implementations.AddressServiceImpl;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/addresses")
public class AddressController {
    private final AddressServiceImpl addressService;

    public AddressController(AddressServiceImpl addressService) {
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
        return ResponseEntity.ok(addresses);
    }

    @GetMapping("/{addressId}")
    public ResponseEntity<AddressDTO> GetAddress(@PathVariable UUID addressId) {
        try {
            var address = addressService.getById(addressId);
            return ResponseEntity.ok(address);
        } catch (AddressNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("")
    public ResponseEntity<String> SaveAddress(@RequestBody AddressDTO addressDTO) {
        if (addressDTO == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("ERROR: Could not save address to the DB.");
        }
        if (addressService.addressExists(addressDTO)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ERROR: Address already exists.");
        }
        addressService.saveToDB(addressDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("Address saved successfully");
    }

    @GetMapping("/get-all-users-by-address/{addressId}")
    public ResponseEntity<Page<UserDTO>> GetAllUsersByAddress(@PathVariable UUID addressId,
                                              @RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "10") int pageSize,
                                              @RequestParam(defaultValue = "firstName") String sortBy,
                                              @RequestParam(defaultValue = "asc") String sortDir) {
        try {
            var users = addressService.getAllUsersByAddress(addressId, page, pageSize, sortBy, sortDir);
            return ResponseEntity.ok(users);
        } catch (AddressNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

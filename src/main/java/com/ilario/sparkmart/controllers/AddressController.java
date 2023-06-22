package com.ilario.sparkmart.controllers;

import com.ilario.sparkmart.dto.AddressDTO;
import com.ilario.sparkmart.dto.UserDTO;
import com.ilario.sparkmart.services.implementations.AddressServiceImpl;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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

    @GetMapping("/get-all-users-by-address/{addressId}")
    public ResponseEntity<Page<UserDTO>> GetAllUsersByAddress(@PathVariable UUID addressId,
                                              @RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "10") int pageSize,
                                              @RequestParam(defaultValue = "firstName") String sortBy,
                                              @RequestParam(defaultValue = "asc") String sortDir) {
        var address = addressService.getById(addressId);
        Pageable pageable = PageRequest.of(
                page,
                pageSize,
                sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());
        if(address == null) {
            return new ResponseEntity<>(new PageImpl<>(new ArrayList<UserDTO>(), pageable, 0), HttpStatus.OK);
        }
        var users = addressService.getAllUsersByAddress(addressId, pageable);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}

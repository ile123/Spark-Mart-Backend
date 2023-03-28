package com.ilario.sparkmart.controllers;

import com.ilario.sparkmart.dto.AddressDTO;
import com.ilario.sparkmart.dto.UserDTO;
import com.ilario.sparkmart.services.implementations.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserServiceImpl userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public ResponseEntity<List<UserDTO>> GetAllUsers() {
        var users = userService.getAllUsers();
        if(users.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> GetUser(@PathVariable("userId")UUID userId) {
        var userDTO = userService.getUserById(userId);
        if(userDTO == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<String> SaveUser(@RequestBody UserDTO userDTO) {
        if(userDTO == null) {
            return new ResponseEntity<>("ERROR: User could not be saved!", HttpStatus.BAD_REQUEST);
        }
        userService.saveUser(userDTO);
        return new ResponseEntity<>("User was saved successfully!", HttpStatus.OK);
    }
    @PutMapping("/update-user/{userId}")
    public ResponseEntity<String> UpdateUser(@PathVariable("userId") UUID userId, @RequestBody UserDTO userDTO) {
        if(userDTO == null) {
            return new ResponseEntity<>("ERROR: Changes to the User could not be made!", HttpStatus.BAD_REQUEST);
        }
        userService.updateUser(userId, userDTO);
        return new ResponseEntity<>("User was changed successfully!", HttpStatus.OK);
    }

    @PutMapping("/change-address/{userId}")
    public ResponseEntity<String> UpdateAddress(@PathVariable("userId") UUID userId, @RequestBody AddressDTO addressDTO) {
        if(addressDTO == null) {
            return new ResponseEntity<>("ERROR: Changes to the Address could not be made!", HttpStatus.BAD_REQUEST);
        }
        userService.updateAddress(userId, addressDTO);
        return new ResponseEntity<>("User address changed successfully!", HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> DeleteUser(@PathVariable("userId") UUID userId) {
        userService.deleteUser(userId);
        return new ResponseEntity<>("User deleted successfully!", HttpStatus.OK);
    }
}

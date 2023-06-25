package com.ilario.sparkmart.controllers;

import com.ilario.sparkmart.dto.AddressDTO;
import com.ilario.sparkmart.dto.ChangePasswordDTO;
import com.ilario.sparkmart.dto.UserDTO;
import com.ilario.sparkmart.services.implementations.UserServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserServiceImpl userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public ResponseEntity<Page<UserDTO>> GetAllUsers(
                @RequestParam(defaultValue = "0") int page,
                @RequestParam(defaultValue = "10") int pageSize,
                @RequestParam(defaultValue = "firstName") String sortBy,
                @RequestParam(defaultValue = "asc") String sortDir,
                @RequestParam(defaultValue = "customer") String userType,
                @RequestParam(defaultValue = "") String keyword) {
        var users = userService.getAll(page, pageSize, sortBy, sortDir, userType, keyword);
        if(users.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> GetUser(@PathVariable("userId")UUID userId) {
        var userDTO = userService.getById(userId);
        if(userDTO == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @GetMapping("/find-by-email/{userEmail}")
    public ResponseEntity<UserDTO> GetUserByEmail(@PathVariable("userEmail") String email) {
        if(email.isEmpty() || email.isBlank()) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        var user = userService.getUserByEmail(email);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/update-user/{userId}")
    public ResponseEntity<String> UpdateUser(@PathVariable("userId") UUID userId, @RequestBody UserDTO userDTO) {
        if(userDTO == null) {
            return new ResponseEntity<>("ERROR: Changes to the User could not be made!", HttpStatus.BAD_REQUEST);
        }
        userService.update(userId, userDTO);
        return new ResponseEntity<>("User was changed successfully!", HttpStatus.OK);
    }

    @PatchMapping("/change-password/{userId}")
    public ResponseEntity<String> ChangePassword(@PathVariable("userId") UUID userId, @RequestBody ChangePasswordDTO newPassword) {
        var user = userService.getById(userId);
        if(user == null) {
            return new ResponseEntity<>("ERROR: User dose not exist!", HttpStatus.BAD_REQUEST);
        }
        userService.changeUserPassword(userId, newPassword.newPassword());
        return new ResponseEntity<>("Change password success!", HttpStatus.OK);
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
        userService.delete(userId);
        return new ResponseEntity<>("User deleted successfully!", HttpStatus.OK);
    }
}

package com.ilario.sparkmart.controllers;

import com.ilario.sparkmart.dto.AddressDTO;
import com.ilario.sparkmart.dto.ChangePasswordDTO;
import com.ilario.sparkmart.dto.UserDTO;
import com.ilario.sparkmart.exceptions.addresses.AddressCouldNotBeMappedException;
import com.ilario.sparkmart.exceptions.addresses.AddressNotFoundException;
import com.ilario.sparkmart.exceptions.users.UserNotFoundException;
import com.ilario.sparkmart.services.implementations.UserServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> GetUser(@PathVariable("userId")UUID userId) {
        try {
            var user = userService.getById(userId);
            return ResponseEntity.ok(user);
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/find-by-email/{userEmail}")
    public ResponseEntity<UserDTO> GetUserByEmail(@PathVariable("userEmail") String email) {
        if(email.isEmpty() || email.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        try {
            var user = userService.getUserByEmail(email);
            return ResponseEntity.ok(user);
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/update-user/{userId}")
    public ResponseEntity<String> UpdateUser(@PathVariable("userId") UUID userId, @RequestBody UserDTO userDTO) {
        if(userDTO == null) {
            return ResponseEntity.badRequest().build();
        }
        try {
            userService.update(userId, userDTO);
            return ResponseEntity.ok("User was changed successfully!");
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/change-password/{userId}")
    public ResponseEntity<String> ChangePassword(@PathVariable("userId") UUID userId, @RequestBody ChangePasswordDTO newPassword) {
        try {
            userService.changeUserPassword(userId, newPassword.newPassword());
            return ResponseEntity.ok("User password was updated successfully.");
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/change-address/{userId}")
    public ResponseEntity<String> UpdateAddress(@PathVariable("userId") UUID userId, @RequestBody AddressDTO addressDTO) {
        if(addressDTO == null) {
            return ResponseEntity.badRequest().build();
        }
        try {
            userService.updateAddress(userId, addressDTO);
            return ResponseEntity.ok("User address changed successfully!");
        } catch (UserNotFoundException | AddressCouldNotBeMappedException | AddressNotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> DeleteUser(@PathVariable("userId") UUID userId) {
        try {
            userService.delete(userId);
            return ResponseEntity.ok("User deleted successfully!");
        }
        catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}

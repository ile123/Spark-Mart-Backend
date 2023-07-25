package com.ilario.sparkmart.controllers;

import com.ilario.sparkmart.dto.AddressDTO;
import com.ilario.sparkmart.dto.ChangePasswordDTO;
import com.ilario.sparkmart.dto.UserDTO;
import com.ilario.sparkmart.services.implementations.UserServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
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
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> GetUser(@PathVariable("userId")UUID userId) {
        var userDTO = Optional.of(userService.getById(userId));
        if(userDTO.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return userDTO.map(x-> ResponseEntity.ok(x))
                .orElse(ResponseEntity.notFound().build());
    }

    /*@GetMapping("/find-by-email/{userEmail}")
    public ResponseEntity<UserDTO> GetUserByEmail(@PathVariable("userEmail") String email) {
        if(email.isEmpty() || email.isBlank()) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        var user = userService.getUserByEmail(email);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }*/

    @PutMapping("/update-user/{userId}")
    public ResponseEntity<String> UpdateUser(@PathVariable("userId") UUID userId, @RequestBody UserDTO userDTO) {
        if(userDTO == null) {
            return ResponseEntity.badRequest().build();
        }
        userService.update(userId, userDTO);
        return ResponseEntity.ok("User was changed successfully!");
    }

    @PatchMapping("/change-password/{userId}")
    public ResponseEntity<String> ChangePassword(@PathVariable("userId") UUID userId, @RequestBody ChangePasswordDTO newPassword) {
        var user = Optional.of(userService.getById(userId));
        if(user.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        userService.changeUserPassword(userId, newPassword.newPassword());
        return ResponseEntity.ok("User password was updated successfully.");
    }

    @PutMapping("/change-address/{userId}")
    public ResponseEntity<String> UpdateAddress(@PathVariable("userId") UUID userId, @RequestBody AddressDTO addressDTO) {
        if(addressDTO == null) {
            return ResponseEntity.badRequest().build();
        }
        userService.updateAddress(userId, addressDTO);
        return ResponseEntity.ok("User address changed successfully!");
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> DeleteUser(@PathVariable("userId") UUID userId) {
        var user = Optional.of(userService.getById(userId));
        if(user.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        userService.delete(userId);
        return ResponseEntity.ok("User deleted successfully!");
    }
}

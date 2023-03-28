package com.ilario.sparkmart.controllers;

import com.ilario.sparkmart.models.Role;
import com.ilario.sparkmart.services.implementations.RoleServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/roles")
public class RoleController {

    private final RoleServiceImpl roleService;

    public RoleController(RoleServiceImpl roleService) {
        this.roleService = roleService;
    }

    @GetMapping("/{roleId}")
    public ResponseEntity<Role> GetRole(@PathVariable("roleId") UUID roleId) {
        var role = roleService.getRole(roleId);
        if(role.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(role.get(), HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<List<Role>> GetAllRoles() {
        var roles = roleService.getAllRoles();
        if(roles.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        else {
            return new ResponseEntity<>(roles, HttpStatus.OK);
        }
    }

    @PostMapping("")
    public ResponseEntity<String> SaveRole(@RequestBody Role role) {
        if(role == null) {
            return new ResponseEntity<>("ERROR: Role could not be saved!", HttpStatus.BAD_REQUEST);
        }
        roleService.saveRole(role);
        return new ResponseEntity<>("Role was saved successfully!", HttpStatus.OK);
    }
}
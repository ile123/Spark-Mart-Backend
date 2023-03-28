package com.ilario.sparkmart.services;

import com.ilario.sparkmart.models.Role;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IRoleService {
    Optional<Role> getRole(UUID id);
    List<Role> getAllRoles();
    void saveRole(Role role);
}
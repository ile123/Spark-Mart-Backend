package com.ilario.sparkmart.services.implementations;

import com.ilario.sparkmart.exceptions.roles.RoleNotFoundException;
import com.ilario.sparkmart.models.Role;
import com.ilario.sparkmart.repositories.IRoleRepository;
import com.ilario.sparkmart.services.IRoleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RoleServiceImpl implements IRoleService {

    private final IRoleRepository roleRepository;

    public RoleServiceImpl(IRoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Optional<Role> getRole(UUID id) {
        var role = roleRepository.findById(id);
        try {
            if(role.isPresent()) {
                return role;
            } else {
                throw new RoleNotFoundException("Role not found");
            }
        } catch (RoleNotFoundException exception) {
            System.out.println(exception.getMessage());
        }
        return null;
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public void saveRole(Role role) {
        roleRepository.save(role);
    }

}
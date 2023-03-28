package com.ilario.sparkmart.roles;

import com.ilario.sparkmart.models.Role;
import com.ilario.sparkmart.repositories.IRoleRepository;
import com.ilario.sparkmart.services.implementations.RoleServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
public class RoleServiceTests {

    @Mock
    private IRoleRepository roleRepository;
    private RoleServiceImpl roleService;
    @BeforeEach
    void setUp() {
        roleService = new RoleServiceImpl(roleRepository);
    }

    @Test
    void GetALlRolesTest() {
        roleService.getAllRoles();
        Mockito.verify(roleRepository).findAll();
    }

    @Test
    void GetRoleById() {
        roleRepository.save(new Role("Employee"));
        var roles = roleRepository.findAll();
        var temp = roles.get(0);
        roleService.getRole(temp.getId());
        ArgumentCaptor<Role> roleArgumentCaptor =
                ArgumentCaptor.forClass(Role.class);
        Mockito.verify(roleRepository).findById(roleArgumentCaptor.capture().getId());
        var capturedRole = roleArgumentCaptor.getValue();
        assertThat(capturedRole).isEqualTo(temp);
    }
}

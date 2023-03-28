package com.ilario.sparkmart.roles;

import com.ilario.sparkmart.models.Role;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class RoleTests {
    @Test
    void CreateRoleTest() {
        var role = new Role();
        assertThat(role).isInstanceOf(Role.class);
    }

    @Test
    void CreateRoleWithNameTest() {
        var role = new Role("Employee");
        assertThat(role.getName()).isNotEqualTo(new String());
    }
}

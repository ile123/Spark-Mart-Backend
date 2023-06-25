package com.ilario.sparkmart.users;

import com.ilario.sparkmart.models.User;
import com.ilario.sparkmart.repositories.IUserRepository;
import com.ilario.sparkmart.services.implementations.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
@ExtendWith(MockitoExtension.class)
public class UserServiceTests {
    @Mock
    private IUserRepository userRepository;
    private UserServiceImpl userService;

    @Test
    void SaveUserTest() {
        userRepository.save(new User());
        Mockito.verify(userRepository).save(new User());
    }

    @Test
    void GetAllUsersTest() {
        userRepository.findAll();
        Mockito.verify(userRepository).findAll();
    }

    @Test
    void GetUserByIdTest() {
        var user = new User();
        userRepository.save(user);
        var temp = userRepository.findAll().get(0);
        userRepository.findById(temp.getId());
        Mockito.verify(userRepository).findById(temp.getId());
    }
}

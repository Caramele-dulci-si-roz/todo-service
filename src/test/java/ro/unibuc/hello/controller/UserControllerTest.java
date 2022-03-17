package ro.unibuc.hello.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import ro.unibuc.hello.data.User;
import ro.unibuc.hello.data.UserRepository;
import ro.unibuc.hello.dto.UserProfile;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserController userController;

    @Test
    void findByNameReturnsUser() {
        var expectedUser = new User("test", "test");
        when(userRepository.findByUsername(expectedUser.getUsername())).thenReturn(Optional.of(expectedUser));

        var expectedBody = new UserProfile(expectedUser.getUsername());
        Assertions.assertEquals(expectedBody, userController.findByName(expectedUser.getUsername()).getBody());
    }

    @Test
    void findByNameReturnsNotFound() {
        when(userRepository.findByUsername(any())).thenReturn(Optional.empty());
        Assertions.assertEquals(HttpStatus.NOT_FOUND, userController.findByName("not found").getStatusCode());
    }
}
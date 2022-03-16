package ro.unibuc.hello.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ro.unibuc.hello.data.User;
import ro.unibuc.hello.data.UserRepository;
import ro.unibuc.hello.dto.UserProfile;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class UserControllerTest {

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserController userController;

    @Test
    void findByNameReturnsUser() {
        var expectedUser = new User("test", "test");
        when(userRepository.findByUsername(expectedUser.getUsername())).thenReturn(Optional.of(expectedUser));

        var expectedResponse = new ResponseEntity<>(new UserProfile(expectedUser.getUsername()), HttpStatus.OK);
        Assertions.assertEquals(expectedResponse.getBody(), userController.findByName(expectedUser.getUsername()).getBody());
    }

    @Test
    void findByNameReturnsNotFound() {
        var expectedUser = new User("test", "test");
        when(userRepository.findByUsername(expectedUser.getUsername())).thenReturn(Optional.of(expectedUser));
        when(userRepository.findByUsername(any())).thenReturn(Optional.empty());

        var expectedResponse = ResponseEntity.notFound().build();
        Assertions.assertEquals(expectedResponse.getStatusCode(), userController.findByName("not found").getStatusCode());
    }
}
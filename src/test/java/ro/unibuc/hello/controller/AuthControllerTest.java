package ro.unibuc.hello.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import ro.unibuc.hello.config.JwtTokenUtil;
import ro.unibuc.hello.data.User;
import ro.unibuc.hello.data.UserRepository;
import ro.unibuc.hello.dto.UserLogin;
import ro.unibuc.hello.dto.UserRegister;

import java.util.Objects;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    AuthenticationManager authenticationManager;

    @Mock
    JwtTokenUtil jwtTokenUtil;

    @InjectMocks
    AuthController authController;

    @Test
    void register() {
        var expectedPassword = "password";
        var expectedHash = "hash";
        when(passwordEncoder.encode(expectedPassword)).thenReturn(expectedHash);

        var expectedUser = new User("username", expectedHash);
        authController.register(new UserRegister(
                expectedUser.getUsername(),
                expectedPassword
        ));

        verify(userRepository).insert(ArgumentMatchers.eq(expectedUser));
    }

    @Test
    void login() {
        var expectedPassword = "password";
        var expectedHash = "hash";
        var expectedToken = "token";
        when(jwtTokenUtil.generateAccessToken(any())).thenReturn(expectedToken);

        var expectedUser = new User("username", expectedHash);
        var expectedAuthentication = new UsernamePasswordAuthenticationToken(expectedUser, null);
        when(authenticationManager.authenticate(any())).thenReturn(expectedAuthentication);

        var result = authController.login(new UserLogin(
                expectedUser.getUsername(),
                expectedPassword
        ));
        Assertions.assertEquals(expectedToken, Objects.requireNonNull(result.getHeaders().get(HttpHeaders.AUTHORIZATION)).get(0));
        Assertions.assertEquals(expectedToken, Objects.requireNonNull(result.getBody()).getToken());
    }
}
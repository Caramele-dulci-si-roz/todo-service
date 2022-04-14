package ro.unibuc.hello.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.unibuc.hello.config.JwtTokenUtil;
import ro.unibuc.hello.data.User;
import ro.unibuc.hello.data.UserRepository;
import ro.unibuc.hello.dto.AuthenticationData;
import ro.unibuc.hello.dto.UserLogin;
import ro.unibuc.hello.dto.UserRegister;

import javax.validation.Valid;

@RequestMapping("/auth")
@RequiredArgsConstructor
@RestController
@Slf4j
public class AuthController {

    final UserRepository userRepository;
    final PasswordEncoder passwordEncoder;
    final AuthenticationManager authenticationManager;
    final JwtTokenUtil jwtTokenUtil;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationData> register(@Valid @RequestBody UserRegister userSignUp) {
        try {
            userRepository.insert(new User(
                    userSignUp.getUsername(),
                    passwordEncoder.encode(userSignUp.getPassword())
            ));

            return authenticatedBody(userSignUp.getUsername(), userSignUp.getPassword(), "Registration successful");
        } catch (Exception e) {
            log.error("Creating new account failed due to errors", e);
            return new ResponseEntity<>(new AuthenticationData(
                    "",
                    e.getMessage()
            ), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationData> login(@Valid @RequestBody UserLogin userLogin) {
        try {
            return authenticatedBody(userLogin.getUsername(), userLogin.getPassword(), "Login successful");
        } catch (BadCredentialsException e) {
            log.warn("Bad credentials entered");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            return new ResponseEntity<>(new AuthenticationData(
                    "",
                    e.getMessage()
            ), HttpStatus.BAD_REQUEST);
        }
    }

    private ResponseEntity<AuthenticationData> authenticatedBody(String username, String password, String message) {
        Authentication authenticate = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(username, password));

        var user = (User) authenticate.getPrincipal();
        var token = jwtTokenUtil.generateAccessToken(user);

        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, token)
                .body(new AuthenticationData(
                        token,
                        message
                ));
    }
}

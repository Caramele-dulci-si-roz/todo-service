package ro.unibuc.hello.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ro.unibuc.hello.data.User;
import ro.unibuc.hello.data.UserRepository;
import ro.unibuc.hello.dto.AuthenticationData;
import ro.unibuc.hello.dto.UserProfile;
import ro.unibuc.hello.dto.UserRegister;

@Controller
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationData> register(@RequestBody UserRegister userSignUp) {
        try {
            userRepository.insert(new User(userSignUp.getName(), userSignUp.getPassword()));
            return new ResponseEntity<>(new AuthenticationData(
                    "not implemented",
                    "Registration is successful"
            ), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(new AuthenticationData(
                    "",
                    e.getMessage()
            ), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/user/{name}")
    public ResponseEntity<UserProfile> findByName(@PathVariable String name) {
        var found = userRepository.findByName(name);
        return found.map(user -> new ResponseEntity<>(
                new UserProfile(user.getName()),
                HttpStatus.OK
        )).orElseGet(() -> ResponseEntity.notFound().build());
    }
}

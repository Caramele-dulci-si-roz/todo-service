package ro.unibuc.hello.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.unibuc.hello.data.UserRepository;
import ro.unibuc.hello.dto.UserProfile;


@RequestMapping("/user")
@RestController
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/{name}")
    public ResponseEntity<UserProfile> findByName(@PathVariable String name) {
        var found = userRepository.findByUsername(name);
        return found.map(user -> new ResponseEntity<>(
                new UserProfile(user.getUsername()),
                HttpStatus.OK
        )).orElseGet(() -> ResponseEntity.notFound().build());
    }
}

package vn.spring.crud.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.spring.crud.entity.User;
import vn.spring.crud.service.UserService;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("users")
public class UserController {

    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        try {
            return ResponseEntity.ok(this.userService.createUser(user));
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(this.userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return this.userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User body) {
        try {
            return ResponseEntity.ok(this.userService.updateUser(id, body));
        }
        catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        try {
            this.userService.deleteUser(id);
        }
        catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
        return null;
    }
}

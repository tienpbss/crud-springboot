package vn.spring.crud.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.spring.crud.entity.ApiResponse;
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
    public ResponseEntity<ApiResponse<User>> createUser(@RequestBody @Valid User user) {
        User createdUser = userService.createUser(user);
        return ResponseEntity.ok(ApiResponse.success("Create user successfully", createdUser));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
        return ResponseEntity.ok(ApiResponse.success("Return all users", userService.getAllUsers()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Return user with id: " + id, userService.getUserById(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> updateUser(@PathVariable Long id, @RequestBody @Valid User body) {
        return ResponseEntity.ok(ApiResponse.success("Update user with id: " + id, userService.updateUser(id, body)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.success("Deleted user with id: " + id));
    }
}

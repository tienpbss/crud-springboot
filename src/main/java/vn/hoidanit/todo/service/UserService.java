package vn.hoidanit.todo.service;

import org.springframework.stereotype.Service;
import vn.hoidanit.todo.entity.User;
import vn.hoidanit.todo.repository.UserRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user) {
        if (this.userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    };

    public Optional<User> getUserById(Long id) {
        return this.userRepository.findById(id);
    };

    public User updateUser(Long id, User input) {
        if (!this.userRepository.existsById(id)) {
            throw new NoSuchElementException("User not found");
        }
        if (this.userRepository.existsByEmail(input.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        User user = this.userRepository.findById(id).get();
        user.setEmail(input.getEmail());
        user.setUsername(input.getUsername());
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        if (!this.userRepository.existsById(id)) {
            throw new NoSuchElementException("User not found");
        }
        this.userRepository.deleteById(id);
    }


}
